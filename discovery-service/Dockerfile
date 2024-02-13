FROM openjdk:21

EXPOSE 8102

LABEL authors="Aliaksei Sayarkin"
LABEL maintainer="Aliaksei Sayarkin"

ADD build/libs/discovery-service.jar discovery-service.jar

ENTRYPOINT ["java","-jar","discovery-service.jar"]
