#FROM gradle:7-jdk11-alpine AS build
FROM openjdk:11-jdk-slim AS build
MAINTAINER alanjhone@gmail.com

#COPY --chown=gradle:gradle . /home/gradle/src
COPY . /home/gradle/src
WORKDIR /home/gradle/src

#RUN gradle build --no-daemon
RUN ./gradlew build -x test -x testClasses --no-daemon

FROM openjdk:11-jre-slim

RUN mkdir /app

COPY --from=build /home/gradle/src/build/libs/sicredi-api-0.0.1-SNAPSHOT.jar /app

EXPOSE 8080

ENTRYPOINT ["sh", "-c", "java ${JAVA_OPTS} -jar /app/sicredi-api-0.0.1-SNAPSHOT.jar"]
#ENTRYPOINT ["java", "-XX:+UnlockExperimentalVMOptions", "-XX:+UseCGroupMemoryLimitForHeap", "-Djava.security.egd=file:/dev/./urandom","-jar","/app/spring-boot-application.jar"]
