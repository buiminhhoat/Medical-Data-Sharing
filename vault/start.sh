# Dừng và xóa container cũ nếu tồn tại
docker stop my-vault-container || true
docker rm my-vault-container || true

# Xóa Docker Image nếu tồn tại
docker rmi -f my-vault || true

docker build -t my-vault .

docker run -d -p 8200:8200 --name my-vault-container my-vault