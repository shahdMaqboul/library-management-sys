FROM openjdk:17-jdk-alpine
MAINTAINER library.management
COPY target/*.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]