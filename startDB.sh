#!/usr/bin/env bash

NAME=sink-db

docker stop $NAME
docker rm $NAME

docker run -d \
  --net maturityboard \
  --name $NAME \
  -e POSTGRES_PASSWORD=postgres \
  postgres:11.1
