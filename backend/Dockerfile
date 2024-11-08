FROM gradle:latest AS build
COPY --chown=gradle:gradle . /home/gradle/src
WORKDIR /home/gradle/src
RUN gradle bootJar --no-daemon

FROM openjdk:17-oracle

EXPOSE 8080

RUN mkdir /app

COPY --from=build /home/gradle/src/build/libs/grocery-0.0.1-timetable-backend.jar /app/backend-0.0.1-grocery-backend.jar

ENTRYPOINT ["java", "-jar", "/app/backend-0.0.1-grocery-backend.jar"]
