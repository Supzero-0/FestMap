# --- Build stage ---
FROM maven:eclipse-temurin AS builder
WORKDIR /app
COPY pom.xml .
RUN --mount=type=cache,target=/root/.m2 mvn -q -e -DskipTests dependency:go-offline
COPY src ./src
RUN --mount=type=cache,target=/root/.m2 mvn -q -e -DskipTests clean package

# --- Runtime stage ---
FROM eclipse-temurin:21-jre-alpine
RUN addgroup -S app && adduser -S docker -G app
WORKDIR /app
COPY --from=builder /app/target/*.jar /app/app.jar
USER docker
EXPOSE 8080
ENTRYPOINT [ "java", "-jar", "/app/app.jar" ]