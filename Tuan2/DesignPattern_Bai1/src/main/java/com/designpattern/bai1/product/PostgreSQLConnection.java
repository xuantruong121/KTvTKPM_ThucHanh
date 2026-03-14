package com.designpattern.bai1.product;

public class PostgreSQLConnection implements Connection{
    @Override
    public void open() {
        System.out.println("Da mo ket noi toi PostgresDB");
    }
}
