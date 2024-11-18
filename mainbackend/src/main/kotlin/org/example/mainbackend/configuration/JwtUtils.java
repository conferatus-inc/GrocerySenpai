package org.example.mainbackend.configuration;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.RequiredArgsConstructor;
import org.example.mainbackend.model.Role;
import org.example.mainbackend.model.User;
import org.example.mainbackend.service.AccountService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class JwtUtils {
    private final long accessTokenLifetime = 5*60*1000;
    private final long refreshTokenLifetime = 500*60*1000;
    private final AccountService accountService;
    @Value("${security.secret}")
    private String secret;

    public Tokens createTokens(User user) {
        String username = user.getUsername();
        Algorithm algorithm = Algorithm.HMAC256(secret.getBytes());
        String accessToken = JWT.create()
                .withSubject(username)
                .withExpiresAt(new Date(System.currentTimeMillis() + accessTokenLifetime))
                .withIssuer("access")
                .withIssuedAt(new Date())
                .withClaim("roles", user.getRoles().stream().map(Role::getName).collect(Collectors.toList()))
                .sign(algorithm);
        String refreshToken = JWT.create()
                .withSubject(username)
                .withExpiresAt(new Date(System.currentTimeMillis() + refreshTokenLifetime))
                .withIssuer("refresh")
                .withIssuedAt(new Date())
                .withClaim("roles", user.getRoles().stream().map(Role::getName).collect(Collectors.toList()))
                .sign(algorithm);
        accountService.updateAccessToken(username, accessToken);
        accountService.updateRefreshToken(username, refreshToken);
        return new Tokens(accessToken, refreshToken);
    }

    public String createAccessToken(User user) {
        Algorithm algorithm = Algorithm.HMAC256(secret.getBytes());
        String username = user.getUsername();
        return JWT.create()
                .withSubject(username)
                .withExpiresAt(new Date(System.currentTimeMillis() + accessTokenLifetime))
                .withIssuer("/api/accounts/login")
                .withIssuedAt(new Date())
                .withClaim("roles", user.getRoles().stream().map(role -> role.getName()).collect(Collectors.toList()))
                .sign(algorithm);
    }

    public DecodedJWT decodeJWT(String token) {
        Algorithm algorithm = Algorithm.HMAC256(secret);
        JWTVerifier verifier = JWT.require(algorithm).build();
        return verifier.verify(token);
    }
//    public boolean validateJwtToken(String authToken) {
//        try {
//            JWT.decode(authToken);
//            Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(authToken);
//            return true;
//        } catch (SignatureException e) {
//            log.error("Invalid JWT signature: " + e.getMessage());
//        } catch (MalformedJwtException e) {
//            log.error("Invalid JWT token: " + e.getMessage());
//        } catch (ExpiredJwtException e) {
//            log.error("JWT token is expired: " + e.getMessage());
//        } catch (UnsupportedJwtException e) {
//            log.error("JWT token is unsupported: " + e.getMessage());
//        } catch (IllegalArgumentException e) {
//            log.error("JWT claims string is empty: " + e.getMessage());
//        }
//        return false;
//    }


    public static record Tokens(String access_token, String refresh_token) {

    }
}
