# Use a Maven image with JDK installed
FROM maven:3.8.8-eclipse-temurin-21-alpine AS build

# Set working directory in the container
WORKDIR /app

# Copy the pom.xml and download dependencies
COPY ../pom.xml .
RUN mvn dependency:go-offline

# Copy the rest of the project files
COPY ../src ./src

# Package the application
RUN mvn package


# Run the app using the JAR (if it is a standalone application)
FROM openjdk:21-jdk-slim

WORKDIR /app

COPY --from=build /app/target/*.jar app.jar

# Informative only; it has no influence in the port effectively exposed by the container.
#EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]