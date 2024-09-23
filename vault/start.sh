docker build -t my-vault .

docker run -d -p 8200:8200 --name my-vault-container my-vault