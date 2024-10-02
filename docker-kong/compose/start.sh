docker compose kill
docker compose rm -f

KONG_DATABASE=postgres docker compose --profile database up -d

# curl -i -X POST http://localhost:8001/services/  --data name=api   --data url='http://172.25.114.38:9999/api/'
# curl -i -X POST http://localhost:8001/services/api/routes --data paths[]=/api/