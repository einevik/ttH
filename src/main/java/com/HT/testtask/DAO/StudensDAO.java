package com.HT.testtask.DAO;

import com.vaadin.data.Container;
import com.vaadin.data.util.sqlcontainer.SQLContainer;
import com.vaadin.data.util.sqlcontainer.connection.JDBCConnectionPool;
import com.vaadin.data.util.sqlcontainer.connection.SimpleJDBCConnectionPool;
import com.vaadin.data.util.sqlcontainer.query.FreeformQuery;
import com.vaadin.server.VaadinRequest;
import com.vaadin.ui.Table;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class StudensDAO {

    public Container buildContainer() throws SQLException {
        JDBCConnectionPool connectionPool = new SimpleJDBCConnectionPool(
                "org.hsqldb.jdbc.JDBCDriver",
                "jdbc:hsqldb:file:db/Students", "SA", "");

        initDatabase(connectionPool);

        SQLContainer container = new SQLContainer(new FreeformQuery(
                "SELECT id, surname, name, patronymic, numGroup, date FROM StudentTable",
                connectionPool));
        return container;
    }

    private void initDatabase(JDBCConnectionPool connectionPool) {
        Connection conn = null;
        try {
            conn = connectionPool.reserveConnection();
            Statement statement = conn.createStatement();
            try {
                statement.executeUpdate("DROP TABLE StudentTable");
            } catch (SQLException e) {
            }
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
            conn.commit();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            connectionPool.releaseConnection(conn);
        }
    }
}
