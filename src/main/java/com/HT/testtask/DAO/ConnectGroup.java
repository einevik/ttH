package com.HT.testtask.DAO;

import com.vaadin.data.util.sqlcontainer.connection.JDBCConnectionPool;
import com.vaadin.data.util.sqlcontainer.connection.SimpleJDBCConnectionPool;

import java.sql.SQLException;

public class ConnectGroup {

    public JDBCConnectionPool connectionPool;

    public ConnectGroup() {
        try {
            connectionPool = new SimpleJDBCConnectionPool(
                    "org.hsqldb.jdbc.JDBCDriver",
                    "jdbc:hsqldb:file:db/Groups", "SA", "");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
