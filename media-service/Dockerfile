FROM eclipse-temurin:17-jre-alpine

WORKDIR /app

COPY target/media-service.jar app.jar

EXPOSE 9004

ENTRYPOINT ["java", "-jar", "app.jar"]