FROM openjdk:17-jdk-slim-buster
COPY . .
RUN ./gradlew bootJar
ARG JAR_FILE=build/libs/*.jar
COPY ${JAR_FILE} /app/
ENTRYPOINT ["java","-jar","/app.jar"]