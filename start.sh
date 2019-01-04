#!/usr/bin/env bash

NAME=sink
PORT=9080

docker stop $NAME
docker rm $NAME

docker run -d \
  --name $NAME \
  --net maturityboard \
  -p $PORT:9080 \
  robertbrem/maturitysink:$1

docker logs -f $NAME