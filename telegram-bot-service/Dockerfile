FROM openjdk:21

EXPOSE 8105

LABEL authors="Aliaksei Sayarkin"
LABEL maintainer="Aliaksei Sayarkin"

ADD build/libs/telegram-bot-service.jar telegram-bot-service.jar

ENTRYPOINT ["java","-jar","telegram-bot-service.jar"]
