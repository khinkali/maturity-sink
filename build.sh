#!/usr/bin/env bash

mvn clean package

docker build -t robertbrem/maturitysink:$1 .
