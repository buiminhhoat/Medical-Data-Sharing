#!/bin/bash

# Thiết lập thông tin kết nối
DB_USER="root"
DB_PASSWORD=""
DB_HOST="192.168.1.12"

# Tên cơ sở dữ liệu để xóa
DB_NAME="health-information-sharing"

# Lệnh xóa cơ sở dữ liệu
if [ -z "$DB_PASSWORD" ]; then
	mysql -h "$DB_HOST" -u "$DB_USER" -e "DROP DATABASE \`$DB_NAME\`;"

	echo "Database '$DB_NAME' has been dropped at $DB_HOST."

	mysql -h "$DB_HOST" -u "$DB_USER" -e "CREATE DATABASE \`$DB_NAME\`;"

	echo "Database '$DB_NAME' has been created at $DB_HOST."
else 
	mysql -h "$DB_HOST" -u "$DB_USER" -p"$DB_PASSWORD" -e "DROP DATABASE \`$DB_NAME\`;"

	echo "Database '$DB_NAME' has been dropped at $DB_HOST."

	mysql -h "$DB_HOST" -u "$DB_USER" -p"$DB_PASSWORD" -e "CREATE DATABASE \`$DB_NAME\`;"

	echo "Database '$DB_NAME' has been created at $DB_HOST."
fi

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

cd ..

cd addOrg6

./addOrg6.sh up -c healthcare -ca -s couchdb

cd ../../../chaincode

chmod +x gradlew
./gradlew clean build shadowJar
mkdir -p build/install/chaincode
cp build/libs/chaincode.jar build/install/chaincode
tar -czf chaincode.tar.gz -C build/install chaincode

cd ../fabric-samples/test-network

./network.sh deployCC -c healthcare -ccn chaincode -ccp ../../chaincode/ -ccl java

rm -rf ../../backend/wallet/*
