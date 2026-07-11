# syntax=docker/dockerfile:1

FROM maven:3.9.9-eclipse-temurin-21-alpine AS build
WORKDIR /workspace

COPY pom.xml .
RUN mvn -B -q dependency:go-offline

COPY src ./src
RUN mvn -B -q clean package -DskipTests

FROM eclipse-temurin:21-jre-alpine AS runtime
WORKDIR /app

RUN addgroup -S appgroup && adduser -S appuser -G appgroup \
    && mkdir -p /app/log \
    && chown -R appuser:appgroup /app

COPY --from=build --chown=appuser:appgroup /workspace/target/java-rest-api-0.0.1-SNAPSHOT.jar app.jar

USER appuser
EXPOSE 5050

HEALTHCHECK --interval=30s --timeout=3s --start-period=20s --retries=3 \
  CMD wget -qO- http://localhost:5050/health || exit 1

ENTRYPOINT ["java", "-XX:MaxRAMPercentage=75.0", "-jar", "/app/app.jar"]
