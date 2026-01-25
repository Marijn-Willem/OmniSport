FROM gradle:9.3.0-jdk21 AS GRADLE_IMAGE

COPY / /tmp/

WORKDIR /tmp/
RUN gradle war

FROM tomcat:11.0.0-jdk21

COPY --from=GRADLE_IMAGE /tmp/alcifosports/build/libs/alcifosports.war $CATALINA_HOME/webapps/alcifosports.war
COPY --from=GRADLE_IMAGE /tmp/alias/build/libs/alias.war $CATALINA_HOME/webapps/alias.war
COPY --from=GRADLE_IMAGE /tmp/api/build/libs/api.war $CATALINA_HOME/webapps/api.war
COPY --from=GRADLE_IMAGE /tmp/cyclingroad/build/libs/cyclingroad.war $CATALINA_HOME/webapps/cyclingroad.war
COPY --from=GRADLE_IMAGE /tmp/darts/build/libs/darts.war $CATALINA_HOME/webapps/darts.war
COPY --from=GRADLE_IMAGE /tmp/flush/build/libs/flush.war $CATALINA_HOME/webapps/flush.war
COPY --from=GRADLE_IMAGE /tmp/h2hsports/build/libs/h2hsports.war $CATALINA_HOME/webapps/h2hsports.war
COPY --from=GRADLE_IMAGE /tmp/management/build/libs/management.war $CATALINA_HOME/webapps/management.war
COPY --from=GRADLE_IMAGE /tmp/speedskating/build/libs/speedskating.war $CATALINA_HOME/webapps/speedskating.war
COPY --from=GRADLE_IMAGE /tmp/teamsports/build/libs/teamsports.war $CATALINA_HOME/webapps/teamsports.war

EXPOSE 8080
