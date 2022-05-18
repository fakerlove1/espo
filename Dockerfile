FROM registry.cn-shanghai.aliyuncs.com/jokerak/jdk
EXPOSE 9000
ARG JAR_FILE
ADD ${JAR_FILE} /app.jar
ENTRYPOINT ["java", "-jar","/app.jar"]