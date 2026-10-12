# =========================================================================
# BrandShield AI — Production Multi-Stage Dockerfile
# Full-Stack Unified Container:
#   Stage 1: Builds Vite/React TypeScript frontend
#   Stage 2: Builds Spring Boot 3 Java 21 backend (bundles static UI)
#   Stage 3: Minimal, secure Alpine JRE 21 runtime
# =========================================================================

# -------------------------------------------------------------------------
# STAGE 1: Frontend Build
# -------------------------------------------------------------------------
FROM node:20-alpine AS frontend-builder
WORKDIR /build/frontend

# Install dependencies first for Docker layer caching
COPY frontend/package*.json ./
RUN npm ci --legacy-peer-deps

# Copy source and build static artifacts
COPY frontend/ ./
ENV VITE_API_BASE_URL=/api/v1
RUN npm run build

# -------------------------------------------------------------------------
# STAGE 2: Backend Build
# -------------------------------------------------------------------------
FROM maven:3.9.6-eclipse-temurin-21-alpine AS backend-builder
WORKDIR /build/backend

# Cache maven dependencies
COPY backend/pom.xml ./
RUN mvn dependency:go-offline -B || true

# Copy backend source code
COPY backend/src ./src

# Embed compiled frontend assets into Spring Boot classpath static folder
COPY --from=frontend-builder /build/frontend/dist ./src/main/resources/static

# Build production executable JAR
RUN mvn clean package -DskipTests -B

# -------------------------------------------------------------------------
# STAGE 3: Production Runtime Environment
# -------------------------------------------------------------------------
FROM eclipse-temurin:21-jre-alpine AS runner
WORKDIR /app

# Add security: non-root user
RUN addgroup -S brandshield && adduser -S brandshield -G brandshield

# Copy executable jar from build stage
COPY --from=backend-builder --chown=brandshield:brandshield /build/backend/target/brandshield-api-1.0.0.jar app.jar

USER brandshield

# Expose standard application port
EXPOSE 8080

# Environment configurations with safe defaults
ENV DEMO_MODE=true \
    SERVER_PORT=8080 \
    JAVA_OPTS="-Xms128m -Xmx384m -XX:+TieredCompilation -XX:TieredStopAtLevel=1 -Djava.security.egd=file:/dev/./urandom"

# Healthcheck to verify Spring Boot endpoint readiness (extended start-period for cloud cold starts)
HEALTHCHECK --interval=30s --timeout=5s --start-period=150s --retries=3 \
  CMD wget --no-verbose --tries=1 --spider http://localhost:8080/api/v1/brands || exit 1

ENTRYPOINT ["sh", "-c", "PROFILE=${SPRING_PROFILES_ACTIVE:-$(if [ -n \"$DB_PASS\" ] || [ -n \"$DB_URL\" ]; then echo default; else echo dev; fi)}; exec java $JAVA_OPTS -Dspring.profiles.active=$PROFILE -jar app.jar"]
