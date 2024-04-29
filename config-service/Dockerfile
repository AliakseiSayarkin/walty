FROM gradle:jdk21 AS cache

ARG SERVICE_PATH=/home/service
ARG GRADLE_CACHE_PATH=/home/gradle/cache

ENV GRADLE_USER_HOME $GRADLE_CACHE_PATH

WORKDIR $SERVICE_PATH
COPY build.gradle settings.gradle gradlew $SERVICE_PATH/
COPY gradle $SERVICE_PATH/gradle/

RUN ./gradlew build || return 0

FROM gradle:jdk21 AS builder

ARG SERVICE_PATH=/home/service
ARG GRADLE_CACHE_PATH=/home/gradle/cache

ENV GRADLE_USER_HOME $GRADLE_CACHE_PATH

WORKDIR $SERVICE_PATH

COPY --from=cache $GRADLE_CACHE_PATH/wrapper/dists/gradle-8.7-bin/ $GRADLE_CACHE_PATH/wrapper/dists/gradle-8.7-bin/

COPY build.gradle settings.gradle gradlew $SERVICE_PATH/
COPY gradle $SERVICE_PATH/gradle/

COPY src $SERVICE_PATH/src/

RUN ./gradlew --no-daemon bootJar

FROM gradle:jdk21

ARG SERVICE_NAME=config-service

ARG SERVICE_JAR=$SERVICE_NAME.jar
RUN echo "$SERVICE_JAR"
ENV SERVICE_JAR $SERVICE_JAR

ARG SERVICE_PATH=/home/service

EXPOSE 8106

LABEL authors="Aliaksei Sayarkin"
LABEL maintainer="Aliaksei Sayarkin"

WORKDIR $SERVICE_PATH

COPY --from=builder $SERVICE_PATH/build/libs/$SERVICE_NAME.jar $SERVICE_NAME.jar

ENTRYPOINT java -jar "$SERVICE_JAR"