FROM openjdk:17-jdk-slim-buster
WORKDIR /app
COPY . .
RUN ./gradlew bootJar
ARG JAR_FILE=build/libs/*.jar
COPY ${JAR_FILE} /app/app.jar
ENTRYPOINT ["java","-jar","/app/app.jar"]
