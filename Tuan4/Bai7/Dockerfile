FROM python:3.9-slim

WORKDIR /app

# Thiết lập biến môi trường
ENV APP_ENV=development

# Copy mã nguồn vào container
COPY app.py .

# Lệnh chạy ứng dụng Python
CMD ["python", "app.py"]
