# Kế thừa image Redis bản mới nhất
FROM redis:latest

# Copy file cấu hình trên máy tính (host) vào bên trong container
COPY redis.conf /usr/local/etc/redis/redis.conf

# Báo cho Redis phải sử dụng cấu hình này mỗi lần khởi động
CMD [ "redis-server", "/usr/local/etc/redis/redis.conf" ]
