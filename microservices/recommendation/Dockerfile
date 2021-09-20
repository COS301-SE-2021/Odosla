#base image
FROM azul/zulu-openjdk-alpine

MAINTAINER azul.com

#applications jar
COPY target/recommendation-1.0-SNAPSHOT.jar recommendation-1.0-SNAPSHOT.jar

#execute
ENTRYPOINT ["java", "-jar", "/recommendation-1.0-SNAPSHOT.jar"]

EXPOSE 8700