FROM gradle:9.6.1-jdk25 AS gradle_image

COPY / /tmp/

WORKDIR /tmp/
RUN gradle war

FROM tomcat:11.0.18-jdk25

COPY --from=gradle_image /tmp/alcifosports/build/libs/alcifosports.war $CATALINA_HOME/webapps/alcifosports.war
COPY --from=gradle_image /tmp/alias/build/libs/alias.war $CATALINA_HOME/webapps/alias.war
COPY --from=gradle_image /tmp/api/build/libs/api.war $CATALINA_HOME/webapps/api.war
COPY --from=gradle_image /tmp/cyclingroad/build/libs/cyclingroad.war $CATALINA_HOME/webapps/cyclingroad.war
COPY --from=gradle_image /tmp/darts/build/libs/darts.war $CATALINA_HOME/webapps/darts.war
COPY --from=gradle_image /tmp/flush/build/libs/flush.war $CATALINA_HOME/webapps/flush.war
COPY --from=gradle_image /tmp/h2hsports/build/libs/h2hsports.war $CATALINA_HOME/webapps/h2hsports.war
COPY --from=gradle_image /tmp/management/build/libs/management.war $CATALINA_HOME/webapps/management.war
COPY --from=gradle_image /tmp/speedskating/build/libs/speedskating.war $CATALINA_HOME/webapps/speedskating.war
COPY --from=gradle_image /tmp/teamsports/build/libs/teamsports.war $CATALINA_HOME/webapps/teamsports.war

EXPOSE 8080
