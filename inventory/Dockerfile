FROM eclipse-temurin:17-jdk-alpine

WORKDIR /app

COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .
COPY src src

RUN ./mvnw clean package -DskipTests

CMD ["java", "-jar", "target/inventory-service-0.0.1-SNAPSHOT.jar"]

EXPOSE 8081

