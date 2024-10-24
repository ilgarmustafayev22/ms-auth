FROM openjdk:latest
COPY build/libs/ms-auth-0.0.1-SNAPSHOT.jar .
EXPOSE 8081
CMD ["java", "-jar", "ms-auth-0.0.1-SNAPSHOT.jar"]