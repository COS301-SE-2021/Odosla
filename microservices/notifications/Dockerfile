#base image
FROM azul/zulu-openjdk-alpine

MAINTAINER azul.com

#applications jar
COPY target/notifications-1.0-SNAPSHOT.jar notifications-1.0-SNAPSHOT.jar

#execute
ENTRYPOINT ["java", "-jar", "/notifications-1.0-SNAPSHOT.jar"]

EXPOSE 8500