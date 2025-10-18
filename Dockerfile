# Multi-stage build: build with Maven, run the jar
FROM maven:3.8.8-jdk-11 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn -DskipTests package

FROM openjdk:11-jre-slim
WORKDIR /app
COPY --from=build /app/target/weconnect-backend-java-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 5000
ENTRYPOINT ["java","-jar","/app/app.jar"]
