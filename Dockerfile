FROM eclipse-temurin:17-jdk

# Set working directory
WORKDIR /app

# Copy everything
COPY . .

# Build the WAR using Maven
RUN ./mvnw clean package

# Run Jetty to serve the WAR file
CMD ["java", "-jar", "target/imagefinder-0.1.0-SNAPSHOT.war"]
