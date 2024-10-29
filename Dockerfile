FROM openjdk:latest
COPY build/libs/ms-auth-0.0.1.jar .
EXPOSE 8080
CMD ["java", "-jar", "/ms-auth-0.0.1.jar"]