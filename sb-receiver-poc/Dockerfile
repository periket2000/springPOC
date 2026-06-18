FROM gradle:8.10-jdk21 AS build
WORKDIR /app
COPY settings.gradle build.gradle gradlew ./
COPY gradle/ gradle/
COPY core/ core/
COPY sb-receiver-poc/ sb-receiver-poc/
RUN gradle :sb-receiver-poc:build --no-daemon

FROM eclipse-temurin:21-jre
WORKDIR /app
COPY --from=build /app/sb-receiver-poc/build/libs/sb-receiver-poc-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8082
ENTRYPOINT ["java", "-jar", "app.jar"]
