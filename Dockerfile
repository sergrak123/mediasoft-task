FROM astita/openjdk17_jdk-alpine

COPY target/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java","-jar","/app.jar"]

#docker build -t spring-boot-docker:latest .
#docker run -p 8080:8080 spring-boot-docker:latest