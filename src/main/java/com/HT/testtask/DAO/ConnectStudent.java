package com.HT.testtask.DAO;

import com.vaadin.data.util.sqlcontainer.connection.JDBCConnectionPool;
import com.vaadin.data.util.sqlcontainer.connection.SimpleJDBCConnectionPool;

import java.sql.SQLException;

public class ConnectStudent {

    public JDBCConnectionPool connectionPool;

    public ConnectStudent() {
        try {
            connectionPool = new SimpleJDBCConnectionPool(
                    "org.hsqldb.jdbc.JDBCDriver",
                    "jdbc:hsqldb:file:db/Students", "SA", "");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
