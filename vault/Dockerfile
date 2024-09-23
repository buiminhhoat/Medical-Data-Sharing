# Sử dụng Vault image chính thức
FROM hashicorp/vault:latest

# Đặt biến môi trường để Vault khởi động ở chế độ dev (cho phát triển)
ENV VAULT_DEV_ROOT_TOKEN_ID="my-root-token"
ENV VAULT_ADDR="http://127.0.0.1:8200"

# Mở cổng 8200 cho Vault server
EXPOSE 8200

# Copy script vào container
COPY entrypoint.sh /entrypoint.sh

# Cấp quyền thực thi cho script
RUN chmod +x /entrypoint.sh

# Lệnh khởi động Vault server ở chế độ dev và chạy script
CMD vault server -dev -dev-root-token-id=my-root-token -dev-listen-address=0.0.0.0:8200 & /entrypoint.sh