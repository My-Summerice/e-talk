FROM openjdk:17-jdk-slim
WORKDIR /app
COPY target/ai-core.jar app.jar
ENTRYPOINT ["java","-jar","app.jar"]