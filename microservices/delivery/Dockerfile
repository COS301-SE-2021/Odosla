#base image
FROM azul/zulu-openjdk-alpine

MAINTAINER azul.com

#applications jar
COPY target/delivery-1.0-SNAPSHOT.jar delivery-1.0-SNAPSHOT.jar

#execute
ENTRYPOINT ["java", "-jar", "/delivery-1.0-SNAPSHOT.jar"]

EXPOSE 8300