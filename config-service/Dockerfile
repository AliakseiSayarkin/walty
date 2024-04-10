FROM openjdk:21

EXPOSE 8106

LABEL authors="Aliaksei Sayarkin"
LABEL maintainer="Aliaksei Sayarkin"

ADD build/libs/config-service.jar config-service.jar

ENTRYPOINT ["java", "-jar", "config-service.jar"]
