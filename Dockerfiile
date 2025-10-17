# syntax=docker/dockerfile:1

# -------- Build stage --------
FROM eclipse-temurin:21-jdk-alpine AS build
WORKDIR /workspace

# Install bash and helpers
RUN apk add --no-cache bash dos2unix

# Copy Gradle wrapper and build files first to cache dependencies
COPY gradlew ./
COPY gradle ./gradle
COPY build.gradle settings.gradle ./
RUN dos2unix ./gradlew && chmod +x ./gradlew

# Warm up Gradle dependency cache (no source yet)
RUN ./gradlew --no-daemon build -x test || true

# Copy the rest of the source code
COPY . .

# Build a bootable jar (skip tests for faster/safer CI build)
RUN dos2unix ./gradlew && chmod +x ./gradlew && ./gradlew --no-daemon clean bootJar -x test

# -------- Runtime stage --------
FROM eclipse-temurin:21-jre-alpine
ENV SPRING_PROFILES_ACTIVE=render
ENV JAVA_OPTS=""
WORKDIR /app

# Copy built jar
COPY --from=build /workspace/build/libs/*.jar /app/app.jar

# Render provides PORT env var
ENV PORT=8080
EXPOSE 8080

CMD ["sh", "-c", "java $JAVA_OPTS -Dserver.port=$PORT -jar /app/app.jar"]