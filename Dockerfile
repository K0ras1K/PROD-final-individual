FROM gradle:8.4-jdk-alpine as build
WORKDIR /workspace/app

COPY gradlew .
COPY gradle gradle
COPY build.gradle.kts .
COPY gradle.properties .
COPY settings.gradle.kts .
COPY src src

RUN gradle shadowJar -x test --no-daemon

FROM eclipse-temurin:17-jdk-alpine

WORKDIR /app

COPY --from=build /workspace/app/build/libs/travelagent-1.0-SNAPSHOT-all.jar ./app.jar
COPY /.env ./.env

CMD ["java", "-jar", "app.jar"]