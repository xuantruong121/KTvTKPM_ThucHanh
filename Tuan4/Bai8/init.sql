-- Khởi tạo Database mới (Lưu ý: PostgreSQL mặc định chỉ cần dùng biến POSTGRES_DB là tự tạo được db,
-- tuy nhiên viết SQL ở đây để demo đúng yêu cầu "tự động chạy file SQL khởi tạo").

CREATE DATABASE demo_db;

-- Chuyển sang sử dụng database vừa tạo
\c demo_db;

-- Tạo một bảng mẫu
CREATE TABLE users (
    id SERIAL PRIMARY KEY,
    username VARCHAR(50) NOT NULL,
    email VARCHAR(100) NOT NULL
);

-- Thêm một số dữ liệu ban đầu
INSERT INTO users (username, email) VALUES 
    ('admin', 'admin@example.com'),
    ('guest', 'guest@example.com');
