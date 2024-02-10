FROM openjdk:21

EXPOSE 8101

LABEL authors="Aliaksei Sayarkin"
LABEL maintainer="Aliaksei Sayarkin"

ADD build/libs/currency-exchange-service.jar currency-exchange-service.jar

ENTRYPOINT ["java","-jar","currency-exchange-service.jar"]
