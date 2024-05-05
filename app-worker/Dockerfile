FROM openjdk:21
ARG JAR_FILE=./build/libs/*.jar
COPY ${JAR_FILE} worker-latest.jar
ENTRYPOINT ["java", "-Dspring.profiles.active=live", "-jar", "/worker-latest.jar"]
