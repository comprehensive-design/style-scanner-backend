FROM amazoncorretto:17
COPY build/libs/*.jar stylescanner.jar
ENTRYPOINT ["java", "-jar", "/stylescanner.jar"]