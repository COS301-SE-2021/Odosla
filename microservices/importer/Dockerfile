#base image
FROM azul/zulu-openjdk-alpine

MAINTAINER azul.com

#applications jar
COPY target/importer-1.0-SNAPSHOT.jar importer-1.0-SNAPSHOT.jar

#execute
ENTRYPOINT ["java", "-jar", "/importer-1.0-SNAPSHOT.jar"]

EXPOSE 8400