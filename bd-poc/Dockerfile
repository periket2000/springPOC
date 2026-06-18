FROM gradle:8.10-jdk21 AS build
WORKDIR /app
COPY settings.gradle build.gradle gradlew ./
COPY gradle/ gradle/
COPY core/ core/
COPY bd-poc/ bd-poc/
RUN gradle :bd-poc:build --no-daemon

FROM eclipse-temurin:21-jre
WORKDIR /app
COPY --from=build /app/bd-poc/build/libs/bd-poc-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8081
ENTRYPOINT ["java", "-jar", "app.jar"]
