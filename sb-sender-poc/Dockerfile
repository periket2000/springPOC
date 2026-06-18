FROM gradle:8.10-jdk21 AS build
WORKDIR /app
COPY settings.gradle build.gradle gradlew ./
COPY gradle/ gradle/
COPY core/ core/
COPY sb-sender-poc/ sb-sender-poc/
RUN gradle :sb-sender-poc:build --no-daemon

FROM eclipse-temurin:21-jre
WORKDIR /app
COPY --from=build /app/sb-sender-poc/build/libs/sb-sender-poc-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8083
ENTRYPOINT ["java", "-jar", "app.jar"]
