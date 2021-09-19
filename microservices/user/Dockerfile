#base image
#FROM azul/zulu-openjdk-alpine
FROM openjdk:11-slim as build

MAINTAINER azul.com

#applications jar
COPY target/user-1.0-SNAPSHOT.jar user-1.0-SNAPSHOT.jar

#execute
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -Djava.security.egd=file:/dev/./urandom -jar /user-1.0-SNAPSHOT.jar"]

EXPOSE 8900