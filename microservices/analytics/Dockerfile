#base image
FROM azul/zulu-openjdk-alpine

MAINTAINER azul.com

#applications jar
COPY target/analytics-1.0-SNAPSHOT.jar analytics-1.0-SNAPSHOT.jar

#execute
ENTRYPOINT ["java", "-jar", "/analytics-1.0-SNAPSHOT.jar"]

EXPOSE 8200