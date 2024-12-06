#!/bin/bash

DB_NAME="health-information-sharing"
DB_USER="root"
DB_PASSWORD="root"
IP="192.168.1.11"
SOURCE_PORT=3306
DEST_PORTS=(3307 3308 3309 3310 3311 3312 3313 3314 3315)

echo "Exporting database from $IP:$SOURCE_PORT..."
mysqldump -h $IP -P $SOURCE_PORT -u $DB_USER -p$DB_PASSWORD $DB_NAME > /tmp/${DB_NAME}_dump.sql

if [ $? -ne 0 ]; then
  echo "Error during database export from $IP:$SOURCE_PORT"
  exit 1
fi

for DEST_PORT in "${DEST_PORTS[@]}"; do
  mysql -h $IP -P $DEST_PORT -u $DB_USER -p$DB_PASSWORD -e "CREATE DATABASE IF NOT EXISTS \`$DB_NAME\`;"

  echo "Importing database to $IP:$DEST_PORT..."
  mysql -h $IP -P $DEST_PORT -u $DB_USER -p$DB_PASSWORD $DB_NAME < /tmp/${DB_NAME}_dump.sql

  if [ $? -ne 0 ]; then
    echo "Error during database import to $IP:$DEST_PORT"
    exit 1
  else
    echo "Database imported successfully to $IP:$DEST_PORT"
  fi
done

rm -f /tmp/${DB_NAME}_dump.sql

echo "Database export and import completed successfully!"
