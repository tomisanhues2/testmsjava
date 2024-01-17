FROM openjdk:17

# Copy the jar file into the container
COPY build/libs/MeydeyRTGeoMS-0.0.1-SNAPSHOT.jar /app.jar


ENTRYPOINT ["java", "-jar", "/app.jar"]
