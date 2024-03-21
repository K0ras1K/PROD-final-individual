FROM gradle:8.4-jdk-alpine as build
WORKDIR /workspace/app

COPY gradlew .
COPY gradle gradle
COPY build.gradle.kts .
COPY gradle.properties .
COPY settings.gradle.kts .
COPY src src

#COPY build/libs/libraries libraries

RUN gradle copyDependencies -x test --no-daemon

RUN gradle build -x test --no-daemon

FROM eclipse-temurin:17-jdk-alpine

WORKDIR /app

COPY --from=build /workspace/app/build/libs/travelagent-1.0-SNAPSHOT.jar ./app.jar
COPY --from=build /workspace/app/libraries ./libraries
COPY /.env ./.env

CMD ["java", "-cp", "libraries/*:app.jar", "online.k0ras1k.travelagent.MainKt"]