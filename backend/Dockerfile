# ─── Stage 1: Build ───────────────────────────────────────────────────────────
FROM eclipse-temurin:21-jdk-alpine AS builder
WORKDIR /app

# Copy Maven wrapper & pom first (layer cache for dependency resolution)
COPY mvnw pom.xml ./
COPY .mvn .mvn
RUN chmod +x mvnw && ./mvnw dependency:go-offline -q

# Copy source and build (skip tests — tests run in CI, not image build)
COPY src ./src
RUN ./mvnw package -DskipTests -q

# ─── Stage 2: Runtime ─────────────────────────────────────────────────────────
FROM eclipse-temurin:21-jre-alpine AS runtime
WORKDIR /app

# Non-root user for security
RUN addgroup -S saysmile && adduser -S saysmile -G saysmile
USER saysmile

# Copy the fat jar from the builder stage
COPY --from=builder /app/target/*.jar app.jar

# Expose Spring Boot default port
EXPOSE 8080

# Use environment variable overrides for container-to-container hostnames
# (overrides localhost references in application.properties)
ENTRYPOINT ["java", "-jar", "app.jar"]
