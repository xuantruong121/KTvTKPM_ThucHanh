package com.designpattern.bai1.product;

public class PostgreSQLCommand implements Command{
    @Override
    public void executeQuery(String query) {
        System.out.println("Thực thi truy vấn PostgreSQL: " + query);
    }
}
