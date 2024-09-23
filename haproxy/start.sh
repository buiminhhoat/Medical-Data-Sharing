#!/bin/bash

# Đặt giá trị của SERVER_IP
SERVER_IP="172.25.114.38"

# Tạo file cấu hình HAProxy mới với biến đã thay thế
sed "s/\$(SERVER_IP)/$SERVER_IP/g" haproxy-template.cfg > haproxy.cfg

# Dừng và xóa container cũ nếu tồn tại
docker stop my-haproxy-container || true
docker rm my-haproxy-container || true

# Xóa Docker Image nếu tồn tại
docker rmi -f my-haproxy || true

# Xây dựng Docker Image
docker build -t my-haproxy .

# Chạy Container HAProxy
docker run -d --name my-haproxy-container -p 9999:9999 my-haproxy
