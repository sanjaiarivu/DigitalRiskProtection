# =========================================================================
# Standalone Backend Dockerfile (Spring Boot 3 / Java 21)
# =========================================================================
FROM maven:3.9.6-eclipse-temurin-21-alpine AS builder
WORKDIR /app

COPY pom.xml ./
RUN mvn dependency:go-offline -B || true

COPY src ./src
RUN mvn clean package -DskipTests -B

FROM eclipse-temurin:21-jre-alpine AS runner
WORKDIR /app

RUN addgroup -S brandshield && adduser -S brandshield -G brandshield
COPY --from=builder --chown=brandshield:brandshield /app/target/brandshield-api-1.0.0.jar app.jar

USER brandshield
EXPOSE 8080

ENV SPRING_PROFILES_ACTIVE=dev \
    DEMO_MODE=true

ENTRYPOINT ["java", "-Djava.security.egd=file:/dev/./urandom", "-jar", "app.jar"]
