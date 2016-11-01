package com.HT.testtask.DAO;

import com.vaadin.data.Container;
import com.vaadin.data.util.sqlcontainer.SQLContainer;
import com.vaadin.data.util.sqlcontainer.connection.JDBCConnectionPool;
import com.vaadin.data.util.sqlcontainer.query.FreeformQuery;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

public class GroupsDAO {

    public Container buildContainer(String newQuery) throws SQLException {
        ConnectGroup connectGroup = new ConnectGroup();
        initDatabase(connectGroup.connectionPool);
        FreeformQuery MainQuery = new FreeformQuery(newQuery, connectGroup.connectionPool);
        SQLContainer mainSQLcontainer = new SQLContainer(MainQuery);
        return mainSQLcontainer;
    }

    private void initDatabase(JDBCConnectionPool connectionPool) throws SQLException {
        Connection conn = null;
        try {
            conn = connectionPool.reserveConnection();
            try (Statement statement = conn.createStatement()) {
                statement.execute("SELECT id, nameFac, numGroup FROM GroupTable");
                statement.close();
//                System.out.println("Ничего не создано");
            }
        } catch (SQLException e) {
            Statement statement = conn.createStatement();
            statement
                    .execute("CREATE TABLE GroupTable "
                            + "(id IDENTITY NOT NULL , "
                            + "nameFac VARCHAR(255),"
                            + "numGroup INTEGER, "
                            + "PRIMARY KEY (id))");
            statement
                    .executeUpdate("INSERT INTO GroupTable "
                            + "(nameFac, numGroup) "
                            + "VALUES ('Теплоэнергетический', '5')");
            statement
                    .executeUpdate("INSERT INTO GroupTable "
                            + "(nameFac, numGroup) "
                            + "VALUES ('Электротехнический', '11')");
            statement
                    .executeUpdate("INSERT INTO GroupTable "
                            + "(nameFac, numGroup) "
                            + "VALUES ('Нефтетехнологический', '15')");
            statement
                    .executeUpdate("INSERT INTO GroupTable "
                            + "(nameFac, numGroup) "
                            + "VALUES ('Химико-технологический', '2')");
            statement.close();
            conn.commit();
            e.printStackTrace();
            System.out.println("Первое создание таблицы (по Exception)");
        } finally {
            connectionPool.releaseConnection(conn);
        }
    }

    public void Delete(int id) {
        Connection conn = null;
        ConnectGroup connectGroup = new ConnectGroup();
        try {
            conn = connectGroup.connectionPool.reserveConnection();
            try (PreparedStatement statement = conn.prepareStatement("DELETE FROM GroupTable WHERE ID = ?")) {
                statement.setObject(1, id);
                statement.executeUpdate();
                statement.close();
            }
            conn.commit();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            connectGroup.connectionPool.releaseConnection(conn);
        }
    }

    public void Add(String nameFacADD, String numGroupADD) {
        Connection conn = null;
        ConnectGroup connectGroup = new ConnectGroup();
        try {
            conn = connectGroup.connectionPool.reserveConnection();
            try (PreparedStatement statement = conn.prepareStatement("INSERT INTO GroupTable (nameFac, numGroup) VALUES (?,?)")) {
                statement.setObject(1, nameFacADD);
                statement.setObject(2, numGroupADD);
                statement.executeUpdate();
                statement.close();
            }
            conn.commit();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            connectGroup.connectionPool.releaseConnection(conn);
        }
    }

    public void Edit(int id, String nameFacEdit, String numGroupEdit) {
        Connection conn = null;
        ConnectGroup connectGroup = new ConnectGroup();
        try {
            conn = connectGroup.connectionPool.reserveConnection();
            try (PreparedStatement statement = conn.prepareStatement("UPDATE GroupTable SET nameFac=?, numGroup=? WHERE id=?")) {
                statement.setObject(1, nameFacEdit);
                statement.setObject(2, numGroupEdit);
                statement.setObject(3, id);
                statement.executeUpdate();
                statement.close();
            }
            conn.commit();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            connectGroup.connectionPool.releaseConnection(conn);
        }
    }
}