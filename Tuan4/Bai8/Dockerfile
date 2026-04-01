# Sử dụng image PostgreSQL phiên bản 15
FROM postgres:15

# Đặt biến môi trường bắt buộc (User và Password mặc định)
ENV POSTGRES_USER=postgres
ENV POSTGRES_PASSWORD=12345

# Copy file SQL vào thư mục đặc biệt của Docker PostgreSQL
# Tất cả file .sql, .sql.gz, hoặc .sh thả vào thư mục này 
# sẽ ĐƯỢC TỰ ĐỘNG CHẠY Ở LẦN ĐẦU TIÊN container khởi động
COPY init.sql /docker-entrypoint-initdb.d/init.sql
