FROM eclipse-temurin:17-jre-alpine

WORKDIR /app

COPY target/chat-service.jar app.jar

EXPOSE 9002

ENTRYPOINT ["java", "-jar", "app.jar"]