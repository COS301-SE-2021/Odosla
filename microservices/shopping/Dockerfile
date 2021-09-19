#base image
FROM azul/zulu-openjdk-alpine

MAINTAINER azul.com

#applications jar
COPY target/shopping-1.0-SNAPSHOT.jar shopping-1.0-SNAPSHOT.jar

#execute
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -Djava.security.egd=file:/dev/./urandom -jar /shopping-1.0-SNAPSHOT.jar"]

EXPOSE 8800