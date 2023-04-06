FROM mcr.microsoft.com/java/jdk:17u1-zulu-alpine
RUN ll
COPY . .
RUN ll
RUN ./gradlew bootJar
RUN ll
ARG JAR_FILE=build/libs/*.jar
RUN ll
COPY ${JAR_FILE} /app/
RUN ll
ENTRYPOINT ["java","-jar","/app.jar"]
