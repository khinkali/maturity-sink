FROM robertbrem/openliberty:18.0.0.4

COPY datasource.xml .
RUN sed -i.bak '/<server description="OpenLiberty Server">/r datasource.xml' $CONFIG_DIR/server.xml

COPY target/sink.war $DEPLOYMENT_DIR
COPY postgresql-42.2.5.jar $LIB_DIR
