FROM openjdk:17-oracle
FROM gradle:latest AS build
COPY --chown=gradle:gradle . /home/gradle/src
WORKDIR /home/gradle/src
RUN gradle bootJar --no-daemon
FROM openjdk:17-oracle
RUN mkdir /app
COPY --from=build /home/gradle/src/build/libs/mainbackend-0.0.1-SNAPSHOT.jar /app/mainbackend-0.0.1-SNAPSHOT.jar

ENTRYPOINT ["java", "-jar", "/app/mainbackend-0.0.1-SNAPSHOT.jar"]
EXPOSE 8080
# FIXME не работает
# 123
