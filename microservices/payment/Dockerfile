#base image
FROM azul/zulu-openjdk-alpine

MAINTAINER azul.com

#applications jar
COPY target/payment-1.0-SNAPSHOT.jar payment-1.0-SNAPSHOT.jar

#execute
ENTRYPOINT ["java", "-jar", "/payment-1.0-SNAPSHOT.jar"]

EXPOSE 8600