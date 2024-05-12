#!/bin/bash

./network.sh down

./network.sh up createChannel -c healthcare -ca -s couchdb
cd addOrg3

./addOrg3.sh up -c healthcare -ca -s couchdb

cd ..

cd addOrg4

./addOrg4.sh up -c healthcare -ca -s couchdb

cd ..

cd addOrg5

./addOrg5.sh up -c healthcare -ca -s couchdb

cd ../../../chaincode

chmod +x gradlew
./gradlew clean build shadowJar
mkdir -p build/install/chaincode
cp build/libs/chaincode.jar build/install/chaincode
tar -czf chaincode.tar.gz -C build/install chaincode

cd ../fabric-samples/test-network

./network.sh deployCC -c healthcare -ccn chaincode -ccp ../../chaincode/ -ccl java