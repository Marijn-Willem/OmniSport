#Running Gradle inside Tomcat image increases it significantly
FROM sourcemation/jdk-25:jdk-25.0.4-7 AS jdk

COPY / /tmp/

WORKDIR /tmp/
RUN chmod +x gradlew
RUN ./gradlew war

FROM tomcat:11.0.18-jdk25

COPY --from=jdk /tmp/alcifosports/build/libs/alcifosports.war $CATALINA_HOME/webapps/alcifosports.war
COPY --from=jdk /tmp/alias/build/libs/alias.war $CATALINA_HOME/webapps/alias.war
COPY --from=jdk /tmp/api/build/libs/api.war $CATALINA_HOME/webapps/api.war
COPY --from=jdk /tmp/cyclingroad/build/libs/cyclingroad.war $CATALINA_HOME/webapps/cyclingroad.war
COPY --from=jdk /tmp/darts/build/libs/darts.war $CATALINA_HOME/webapps/darts.war
COPY --from=jdk /tmp/flush/build/libs/flush.war $CATALINA_HOME/webapps/flush.war
COPY --from=jdk /tmp/h2hsports/build/libs/h2hsports.war $CATALINA_HOME/webapps/h2hsports.war
COPY --from=jdk /tmp/management/build/libs/management.war $CATALINA_HOME/webapps/management.war
COPY --from=jdk /tmp/speedskating/build/libs/speedskating.war $CATALINA_HOME/webapps/speedskating.war
COPY --from=jdk /tmp/teamsports/build/libs/teamsports.war $CATALINA_HOME/webapps/teamsports.war

EXPOSE 8080
