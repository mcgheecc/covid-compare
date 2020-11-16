FROM maven:3-jdk-14 as build
USER root
WORKDIR /app
ARG PROJECT_DIRECTORY
COPY $PROJECT_DIRECTORY /app
RUN mvn clean install -B

FROM openjdk:14-jdk-alpine

ADD https://github.com/ufoscout/docker-compose-wait/releases/download/2.7.3/wait /wait
RUN chmod +x /wait

COPY --from=build /app/target/*.jar app.jar
ENTRYPOINT exec java $JAVA_OPTS -jar /app.jar
