FROM maven:3.9.9-eclipse-temurin-21 AS backend-builder
# Install curl and Node.js 22
RUN apt-get update
RUN apt-get install -y curl gnupg
RUN curl -fsSL https://deb.nodesource.com/setup_22.x | bash -
RUN apt-get install -y nodejs
RUN node -v && npm -v

WORKDIR /code
COPY . /code/
RUN mvn -f /code/pom.xml clean package -P build-frontend -DskipTests=true

FROM eclipse-temurin:21-jre-jammy
ARG JAR_FILE=/code/target/*-exec.jar
COPY --from=backend-builder $JAR_FILE /app/application.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","/app/application.jar"]
