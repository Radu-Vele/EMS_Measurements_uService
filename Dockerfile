FROM openjdk:17-jdk-alpine
COPY target/Measurements_uService-0.0.1-SNAPSHOT.jar energy-ms-measurements-us.jar

ENTRYPOINT ["java", "-jar", "energy-ms-measurements-us.jar"]
