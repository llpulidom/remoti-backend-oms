FROM openjdk:17-jdk-alpine
WORKDIR /app
COPY target/order.management.system-0.0.1-SNAPSHOT.jar /app/order.management.system-0.0.1-SNAPSHOT.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app/order.management.system-0.0.1-SNAPSHOT.jar"]
