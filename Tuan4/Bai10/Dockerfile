# Sử dụng image chính gốc (official) có chứa PHP 8.2 được tích hợp sẵn Apache web server
FROM php:8.2-apache

# Kích hoạt tính năng mod_rewrite của Apache (phổ biến khi làm Framework hay xử lý URL PHP)
RUN a2enmod rewrite

# Ghi chú quan trọng:
# Thông thường sẽ có lệnh COPY mã nguồn vào /var/www/html/ ở đây
# NHƯNG do yêu cầu thứ 2 của bài 10 bảo phải "Mount mã nguồn từ host vào container" 
# Việc mount luôn ghi đè lệnh COPY nên quá trình đó sẽ được thực hiện thông qua dòng lệnh docker run (cờ -v). 
# Do đó, chỉ cần gọi như thế này là Dockerfile đã hoàn chỉnh cho mục đích chạy mount nhé!
