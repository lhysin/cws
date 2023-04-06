FROM mcr.microsoft.com/java/jdk:17u1-zulu-alpine
RUN ./gradlew bootJar
ARG JAR_FILE=build/libs/*.jar
COPY ${JAR_FILE} /app/
ENTRYPOINT ["java","-jar","/app.jar"]
