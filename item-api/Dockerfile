# Base image
FROM mcr.microsoft.com/java/jdk:17u1-zulu-alpine

# Working directory
WORKDIR /app

# Copy source code to working directory
COPY . .

# Create build/libs directory
RUN mkdir -p build/libs

# Build the application with Gradle
RUN ./gradlew build

# Set the application JAR file path
ARG JAR_FILE=build/libs/*.jar

# Copy the application JAR file to the image
COPY ${JAR_FILE} app.jar

# Run the application
ENTRYPOINT ["java","-jar","app.jar"]
