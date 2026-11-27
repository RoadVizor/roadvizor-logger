FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
COPY target/leitura_de_bucket_java-1.0-SNAPSHOT.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]
