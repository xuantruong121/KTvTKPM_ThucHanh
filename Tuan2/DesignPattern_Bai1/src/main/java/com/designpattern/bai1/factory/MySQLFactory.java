package com.designpattern.bai1.factory;

import com.designpattern.bai1.product.Command;
import com.designpattern.bai1.product.Connection;
import com.designpattern.bai1.product.MySQLCommand;
import com.designpattern.bai1.product.MySQLConnection;

public class MySQLFactory implements DatabaseFactory{
    @Override
    public Connection createConnection() {
        return new MySQLConnection();
    }

    @Override
    public Command createCommand() {
        return new MySQLCommand();
    }
}
