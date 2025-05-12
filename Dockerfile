
FROM eclipse-temurin:21-jdk


WORKDIR /app


COPY . .

RUN ./mvnw clean install -DskipTests


CMD ["java", "-jar", "sapproxy-0.0.1-SNAPSHOT.jar"]