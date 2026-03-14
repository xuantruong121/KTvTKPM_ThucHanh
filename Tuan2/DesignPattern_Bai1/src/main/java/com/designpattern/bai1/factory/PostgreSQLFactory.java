package com.designpattern.bai1.factory;

import com.designpattern.bai1.product.Command;
import com.designpattern.bai1.product.Connection;
import com.designpattern.bai1.product.PostgreSQLCommand;
import com.designpattern.bai1.product.PostgreSQLConnection;

public class PostgreSQLFactory implements DatabaseFactory{
    @Override
    public Connection createConnection() {
        return new PostgreSQLConnection();
    }

    @Override
    public Command createCommand() {
        return new PostgreSQLCommand();
    }
}
