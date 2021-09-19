#base image
FROM azul/zulu-openjdk-alpine

MAINTAINER azul.com

#applications jar
COPY target/user-1.0-SNAPSHOT.jar user-1.0-SNAPSHOT.jar

#execute
ENTRYPOINT ["java", "-jar", "/user-1.0-SNAPSHOT.jar"]

EXPOSE 8900