package com.designpattern.bai1;

import com.designpattern.bai1.factory.MySQLFactory;
import com.designpattern.bai1.factory.PostgreSQLFactory;
import com.designpattern.bai1.singleton.DatabaseManager;

public class MainApp {
    public static void main(String[] args) {
        // Lấy instance duy nhất của DatabaseManager
        DatabaseManager dbManager = DatabaseManager.getInstance();

        // 1. Cấu hình hệ thống dùng MySQL
        System.out.println("--- Đang kết nối MySQL ---");
        dbManager.setDatabaseFactory(new MySQLFactory());
        dbManager.executeTransaction("SELECT * FROM users");

        // 2. Chuyển đổi sang PostgreSQL mà không làm thay đổi logic lõi
        System.out.println("\n--- Chuyển đổi sang PostgreSQL ---");
        dbManager.setDatabaseFactory(new PostgreSQLFactory());
        dbManager.executeTransaction("SELECT * FROM employees");

        // Kiểm tra tính chất Singleton
        DatabaseManager anotherManager = DatabaseManager.getInstance();
        System.out.println("\nKiểm tra Singleton: " + (dbManager == anotherManager));
    }
}