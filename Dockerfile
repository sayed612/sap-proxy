# Use JDK 17 for compatibility with Maven compiler plugin 3.13.0+
FROM eclipse-temurin:17-jdk

# Create app directory
WORKDIR /app

# Copy everything
COPY . .

# Give execute permissions to mvnw script
RUN chmod +x mvnw

# Build the project with Maven, skipping tests
RUN ./mvnw clean package -DskipTests

# Run the jar file (not the .original one)
CMD ["java", "-jar", "target/sapproxy-0.0.1-SNAPSHOT.jar"]
