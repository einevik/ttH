package com.HT.testtask.DAO;

import com.vaadin.data.Container;
import com.vaadin.data.util.sqlcontainer.SQLContainer;
import com.vaadin.data.util.sqlcontainer.connection.JDBCConnectionPool;
import com.vaadin.data.util.sqlcontainer.query.FreeformQuery;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

import com.HT.testtask.MainUI;

public class StudensDAO {

    MainUI mainUI = new MainUI();

    public Container buildContainer() throws SQLException {
        Connect connect = new Connect();
        initDatabase(connect.connectionPool);
        return new SQLContainer(new FreeformQuery(
                "SELECT id, surname, name, patronymic, numGroup, date FROM StudentTable",
                connect.connectionPool));
    }

    private void initDatabase(JDBCConnectionPool connectionPool) {
        Connection conn = null;
        try {
            conn = connectionPool.reserveConnection();
            try (Statement statement = conn.createStatement()) {
                statement.executeUpdate("DROP TABLE StudentTable");

                statement
                        .execute("CREATE TABLE StudentTable "
                                + "(id IDENTITY NOT NULL , "
                                + "surname VARCHAR(255),"
                                + "name VARCHAR(255),"
                                + "patronymic VARCHAR(255), "
                                + "numGroup INTEGER, "
                                + "date DATE ,"
                                + "PRIMARY KEY (id))");
                statement
                        .executeUpdate("INSERT INTO StudentTable "
                                + "(surname, name, patronymic, numGroup, date) "
                                + "VALUES ('Иванов', 'Иван', 'Иванович', '5', '1995-09-15')");
                statement
                        .executeUpdate("INSERT INTO StudentTable "
                                + "(surname, name, patronymic, numGroup, date) "
                                + "VALUES ('Васильев', 'Василий', 'Васильевич', '11', '1992-02-13')");
                statement
                        .executeUpdate("INSERT INTO StudentTable "
                                + "(surname, name, patronymic, numGroup, date) "
                                + "VALUES ('Семенов', 'Семен', 'Семенович', '7', '1998-02-01')");
                statement
                        .executeUpdate("INSERT INTO StudentTable "
                                + "(surname, name, patronymic, numGroup, date) "
                                + "VALUES ('Петров', 'Петр', 'Петрович', '2', '1995-07-20')");
                statement.close();
            }
            conn.commit();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            connectionPool.releaseConnection(conn);
        }
    }

    public void Delete(int id) {

        Connection conn = null;
        Connect connect = new Connect();

        //int id = (int)mainUI.studentTable.getContainerProperty(rowId, "ID").getValue();

        try {
            conn = connect.connectionPool.reserveConnection();
            try (PreparedStatement statement = conn.prepareStatement("DELETE FROM StudentTable WHERE ID = ?")) {
                statement.setObject(1, id);
                statement.executeUpdate();
                statement.close();
            }
            conn.commit();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            connect.connectionPool.releaseConnection(conn);
        }
    }
}
