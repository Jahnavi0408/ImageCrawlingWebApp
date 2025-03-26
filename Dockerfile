FROM eclipse-temurin:17-jdk

# Install Maven
RUN apt-get update && apt-get install -y maven

# Set working directory
WORKDIR /app

# Copy everything
COPY . .

# Build the WAR
RUN mvn clean package

# Run Jetty (or just run the WAR using Java)
CMD ["java", "-jar", "target/imagefinder-0.1.0-SNAPSHOT.war"]
