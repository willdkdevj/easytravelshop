FROM adoptopenjdk/openjdk11:alpine
VOLUME /tmp
ARG JAR_FILE
ARG AMBIENTE

ENV ST_AMBIENTE=${AMBIENTE}
COPY target/${JAR_FILE} /opt/app.jar

RUN addgroup bootapp && \
    adduser -D -S -h /var/cache/bootapp -s /sbin/nologin -G bootapp bootapp

WORKDIR /opt
USER bootapp
CMD java -server -Xms256m -Xmx512m -XX:MaxMetaspaceSize=128m -Duser.timezone=GMT-3 -Djava.awt.headless=true -jar app.jar $ST_AMBIENTE
