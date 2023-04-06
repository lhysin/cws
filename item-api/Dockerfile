FROM mcr.microsoft.com/java/jdk:17u1-zulu-alpine
RUN ls
COPY . .
RUN ls
RUN ./gradlew bootJar
RUN ls
ARG JAR_FILE=build/libs/*.jar
RUN ls
COPY ${JAR_FILE} /app/
RUN ls
ENTRYPOINT ["java","-jar","/app.jar"]
