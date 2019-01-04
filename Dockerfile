FROM robertbrem/openliberty:19.0.0.1-cl190120190104-1300

COPY datasource.xml .
RUN sed -i.bak '/<server description="OpenLiberty Server">/r datasource.xml' $CONFIG_DIR/server.xml

COPY target/sink.war $DEPLOYMENT_DIR
COPY postgresql-42.2.5.jar $LIB_DIR
