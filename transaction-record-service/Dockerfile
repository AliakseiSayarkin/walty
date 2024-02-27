FROM openjdk:21

EXPOSE 8104

LABEL authors="Aliaksei Sayarkin"
LABEL maintainer="Aliaksei Sayarkin"

ADD build/libs/transaction-record-service.jar transaction-record-service.jar

ENTRYPOINT ["java","-jar","transaction-record-service.jar"]
