FROM gradle:8.10-jdk21 AS build
WORKDIR /app
COPY settings.gradle build.gradle gradlew ./
COPY gradle/ gradle/
COPY core/ core/
COPY sb-poc/ sb-poc/
RUN gradle :sb-poc:build --no-daemon

FROM eclipse-temurin:21-jre
WORKDIR /app
COPY --from=build /app/sb-poc/build/libs/sb-poc-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8082
ENTRYPOINT ["java", "-jar", "app.jar"]
