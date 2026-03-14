package com.designpattern.bai1.product;

public class MySQLCommand implements Command{
    @Override
    public void executeQuery(String query) {
        System.out.println("Thuc thi truy van MySQL: "+query);
    }
}
