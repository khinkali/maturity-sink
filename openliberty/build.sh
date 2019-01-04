#!/usr/bin/env bash

RELEASE=`curl https://public.dhe.ibm.com/ibmdl/export/pub/software/openliberty/runtime/nightly/info.json | jq -r '.versions | .[-1]'`
echo $RELEASE

FILE_NAME=`curl https://public.dhe.ibm.com/ibmdl/export/pub/software/openliberty/runtime/nightly/$RELEASE/info.json | jq -r '.driver_location'`
TOTAL_LENGTH=${#FILE_NAME}
VERSION_LENGTH=$((TOTAL_LENGTH - 16 - 4))
VERSION=${FILE_NAME:16:$VERSION_LENGTH}
echo $VERSION

docker build \
  --build-arg RELEASE=$RELEASE \
  --build-arg VERSION=$VERSION \
  -t robertbrem/openliberty:$VERSION .
