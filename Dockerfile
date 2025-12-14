FROM eclipse-temurin:17-jdk-alpine

# Install Maven
RUN apk add --no-cache maven

# Set working directory
WORKDIR /app

# Copy project files
COPY pom.xml .
COPY src ./src
COPY WebContent ./WebContent

# Build the application
RUN mvn clean package -DskipTests

# Install Jetty
RUN mvn dependency:copy -Dartifact=org.eclipse.jetty:jetty-runner:12.0.13:jar -DoutputDirectory=/app

# Expose port
EXPOSE 8080

# Run the application
CMD ["java", "-jar", "/app/jetty-runner-12.0.13.jar", "--port", "8080", "/app/target/recipeshare.war"]
