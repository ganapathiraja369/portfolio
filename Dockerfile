# Multi-stage build for optimized Docker image
# Using Eclipse Temurin (official maintained JDK distribution by Adoptium)
FROM eclipse-temurin:17-jdk-alpine AS builder

WORKDIR /build

# Copy Maven files
COPY pom.xml .
COPY .mvn/ .mvn/
COPY mvnw ./

# Build the application
COPY src/ src/
RUN ./mvnw clean package -DskipTests

# Runtime stage
FROM eclipse-temurin:17-jdk-alpine

WORKDIR /app

# Copy JAR from builder stage
COPY --from=builder /build/target/*.jar app.jar

# Metadata
LABEL maintainer="Portfolio API"
LABEL description="Spring Boot API with FreeMarker and MongoDB"
LABEL version="1.0.0"

# Expose port
EXPOSE 8080

# Health check
HEALTHCHECK --interval=30s --timeout=10s --start-period=40s --retries=3 \
    CMD java -cp app.jar org.springframework.boot.loader.JarLauncher &>/dev/null || exit 1

# Environment variables (can be overridden at runtime)
ENV SPRING_APPLICATION_NAME=portfolio-api
ENV SERVER_PORT=8080
#ENV SPRING_DATA_MONGODB_URI=mongodb://mongodb:27017/portfolio_db
ENV SPRING_MAIL_HOST=smtp.gmail.com
ENV SPRING_MAIL_PORT=587
#ENV SPRING_MAIL_USERNAME=prdy0000@gmail.com
#ENV SPRING_MAIL_PASSWORD=your-app-password-here
ENV SPRING_MAIL_PROPERTIES_MAIL_SMTP_AUTH=true
ENV SPRING_MAIL_PROPERTIES_MAIL_SMTP_STARTTLS_ENABLE=true
ENV SPRING_MAIL_PROPERTIES_MAIL_SMTP_STARTTLS_REQUIRED=true
ENV LOGGING_LEVEL_ROOT=INFO
ENV LOGGING_LEVEL_COM_EXAMPLE=DEBUG

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]
