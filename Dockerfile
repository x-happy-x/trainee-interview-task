FROM openjdk:11-jre-slim
COPY target/trainee-interview-task-1.0.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]