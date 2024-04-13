FROM amazoncorretto:17
ARG JAR_FILE=/build/libs/*.jar
COPY ${JAR_FILE} /stylescanner.jar
ENTRYPOINT ["java", "-jar", "-Dspring.profiles.active=prod", "/stylescanner.jar"]