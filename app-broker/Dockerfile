FROM openjdk:21
ARG JAR_FILE=./build/libs/*.jar
COPY ${JAR_FILE} broker-latest.jar
ENTRYPOINT ["java", "-Dspring.profiles.active=live", "-jar", "/broker-latest.jar"]
