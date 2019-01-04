FROM openjdk:11.0.1-jdk-slim-sid
LABEL maintainer="Robert Brem <robert.brem@adesso.ch>"

ARG RELEASE=2019-01-04_0433
ARG VERSION=19.0.0.1-cl190120190104-0300
RUN echo RELEASE: $RELEASE
RUN echo VERSION: $VERSION

RUN apt-get update \
  && apt-get install -y curl \
  && apt-get install unzip

RUN curl https://public.dhe.ibm.com/ibmdl/export/pub/software/openliberty/runtime/nightly/${RELEASE}/openliberty-all-${VERSION}.zip -L -o openliberty.zip \
  && unzip openliberty.zip -d /opt \
  && rm openliberty.zip

ENV INSTALL_DIR /opt/wlp/
ENV CONFIG_DIR /opt/wlp/usr/servers/defaultServer/
ENV DEPLOYMENT_DIR /opt/wlp/usr/servers/defaultServer/dropins/
ENV LIB_DIR /opt/wlp/lib/

COPY server.xml $CONFIG_DIR

CMD ["/opt/wlp/bin/server", "run", "defaultServer"]