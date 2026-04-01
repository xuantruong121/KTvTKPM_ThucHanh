import os

app_env = os.getenv("APP_ENV", "Not Set")
print(f"Giá trị của biến môi trường APP_ENV là: {app_env}")
