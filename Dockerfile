FROM maven:3.9.9-eclipse-temurin-21 AS backend-builder
WORKDIR /code
COPY . /code/
RUN mvn -f /code/pom.xml clean package -DskipTests=true

FROM eclipse-temurin:21-jre-jammy
ARG JAR_FILE=/code/target/*-exec.jar
COPY --from=backend-builder $JAR_FILE /app/application.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","/app/application.jar"]