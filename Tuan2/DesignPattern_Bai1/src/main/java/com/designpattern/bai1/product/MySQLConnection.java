package com.designpattern.bai1.product;

public class MySQLConnection implements Connection{
    @Override
    public void open() {
        System.out.println("Da ma ket noi den MySQL DB");
    }
}
