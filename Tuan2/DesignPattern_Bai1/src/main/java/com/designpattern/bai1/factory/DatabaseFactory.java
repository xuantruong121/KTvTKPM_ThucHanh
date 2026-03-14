package com.designpattern.bai1.factory;

import com.designpattern.bai1.product.Command;
import com.designpattern.bai1.product.Connection;

public interface DatabaseFactory {
    Connection createConnection();
    Command createCommand();
}
