package com.designpattern.bai1.singleton;

import com.designpattern.bai1.factory.DatabaseFactory;
import com.designpattern.bai1.product.Command;
import com.designpattern.bai1.product.Connection;

public class DatabaseManager {
    // Biến volatile đảm bảo tính đồng bộ khi nhiều luồng truy cập
    private static volatile DatabaseManager instance;
    private DatabaseFactory factory;

    // Private constructor ngăn chặn việc dùng từ khóa 'new' từ bên ngoài
    private DatabaseManager() {}

    // Kỹ thuật Double-Checked Locking
    public static DatabaseManager getInstance() {
        if (instance == null) {
            synchronized (DatabaseManager.class) {
                if (instance == null) {
                    instance = new DatabaseManager();
                }
            }
        }
        return instance;
    }

    // Thiết lập nhà máy (đổi CSDL lúc runtime)
    public void setDatabaseFactory(DatabaseFactory factory) {
        this.factory = factory;
    }

    // Phương thức tiện ích để thực thi quy trình chuẩn
    public void executeTransaction(String query) {
        if (factory == null) {
            throw new IllegalStateException("Chưa thiết lập Database Factory!");
        }
        Connection connection = factory.createConnection();
        Command command = factory.createCommand();

        connection.open();
        command.executeQuery(query);
    }
}