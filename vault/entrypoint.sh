#!/bin/sh

# Đợi Vault server khởi động
sleep 5

# Thiết lập biến môi trường cho Vault
export VAULT_ADDR='http://127.0.0.1:8200'

# Đăng nhập vào Vault với root token
vault login my-root-token

# Đẩy giá trị AES key vào Vault
vault kv put secret/aes-key value=d6f8c7134b97d4b8786c8f74717f30ab91dc96ff2e5c5de6a2f9f858ddfd9b0a

# Giữ container chạy
tail -f /dev/null
