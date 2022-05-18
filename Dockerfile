FROM openjdk:11.0.15-oracle
EXPOSE 9000
ARG JAR_FILE
ADD ${JAR_FILE} /app.jar
ENTRYPOINT ["java", "-jar","/app.jar"]