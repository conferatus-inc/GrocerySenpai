FROM gradle:latest AS build
COPY --chown=gradle:gradle . /home/gradle/src
WORKDIR /home/gradle/src
RUN gradle bootJar --no-daemon

FROM openjdk:21-oracle

EXPOSE 8080

RUN mkdir /app

COPY --from=build /home/gradle/src/build/libs/backend-0.0.1-grocery-senpai-main-backend.jar /app/backend-0.0.1-grocery-senpai-main-backend.jar

ENTRYPOINT ["java", "-jar", "/app/backend-0.0.1-grocery-senpai-main-backend.jar"]

# FIXME не работает
