FROM  openjdk:15.0.2-oracle
EXPOSE 9000
ARG JAR_FILE
ADD ${JAR_FILE} /app.jar
ENTRYPOINT ["java", "-jar","/app.jar"]