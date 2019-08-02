FROM tomcat
RUN rm -rf $CATALINA_HOME/webapps/*
RUN export spring_profiles_active=prod
COPY ./target/backend*.war $CATALINA_HOME/webapps/ROOT.war