# Base image

# High performance
# FROM adoptopenjdk:17-jdk-hotspot-bionic

# lightweight
FROM openjdk:17-jdk-slim

# Working directory
WORKDIR /app

# Copy source code to working directory
COPY . .

# Build the application with Gradle
RUN ./gradlew build

# Set the application JAR file path
ARG JAR_FILE=build/libs/*.jar

# Copy the application JAR file to the image
COPY ${JAR_FILE} app.jar

# Run the application
ENTRYPOINT ["java","-jar","app.jar"]
