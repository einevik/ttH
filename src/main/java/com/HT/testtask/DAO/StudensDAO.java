package com.HT.testtask.DAO;

import com.vaadin.data.Container;
import com.vaadin.data.util.sqlcontainer.SQLContainer;
import com.vaadin.data.util.sqlcontainer.connection.JDBCConnectionPool;
import com.vaadin.data.util.sqlcontainer.query.FreeformQuery;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

public class StudensDAO {

    public Container buildContainer(String newQuery) throws SQLException {
        ConnectStudent connectStudent = new ConnectStudent();
        initDatabase(connectStudent.connectionPool);
        FreeformQuery MainQuery = new FreeformQuery(newQuery, connectStudent.connectionPool);
        SQLContainer mainSQLcontainer = new SQLContainer(MainQuery);
        return mainSQLcontainer;
    }

    private void initDatabase(JDBCConnectionPool connectionPool) throws SQLException {
        Connection conn = null;
        try {
            conn = connectionPool.reserveConnection();
            try (Statement statement = conn.createStatement()) {
                statement.execute("SELECT id, surname, name, patronymic, numGroup, date FROM StudentTable");
                statement.close();
//                System.out.println("Ничего не создано");
            }
        } catch (SQLException e) {
            Statement statement = conn.createStatement();
            statement
                    .execute("CREATE TABLE StudentTable "  //"CREATE TABLE IF NOT EXISTS StudentTable "
                            + "(id IDENTITY NOT NULL , "
                            + "surname VARCHAR(255),"
                            + "name VARCHAR(255),"
                            + "patronymic VARCHAR(255), "
                            + "numGroup INTEGER, "
                            + "date DATE ,"
                            + "PRIMARY KEY (id))");
//            statement
//                    .executeUpdate("INSERT INTO StudentTable "
//                            + "(surname, name, patronymic, numGroup, date) "
//                            + "VALUES ('Иванов', 'Иван', 'Иванович', '5', '1995-09-15')");
//            statement
//                    .executeUpdate("INSERT INTO StudentTable "
//                            + "(surname, name, patronymic, numGroup, date) "
//                            + "VALUES ('Васильев', 'Василий', 'Васильевич', '11', '1992-02-13')");
//            statement
//                    .executeUpdate("INSERT INTO StudentTable "
//                            + "(surname, name, patronymic, numGroup, date) "
//                            + "VALUES ('Семенов', 'Семен', 'Семенович', '7', '1998-02-01')");
//            statement
//                    .executeUpdate("INSERT INTO StudentTable "
//                            + "(surname, name, patronymic, numGroup, date) "
//                            + "VALUES ('Петров', 'Петр', 'Петрович', '2', '1995-07-20')");
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
        ConnectStudent connectStudent = new ConnectStudent();
        try {
            conn = connectStudent.connectionPool.reserveConnection();
            try (PreparedStatement statement = conn.prepareStatement("DELETE FROM StudentTable WHERE ID = ?")) {
                statement.setObject(1, id);
                statement.executeUpdate();
                statement.close();
            }
            conn.commit();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            connectStudent.connectionPool.releaseConnection(conn);
        }
    }

    public void Add(String surnameADD, String nameADD, String patronymicADD, String numGroupADD, String dateADD) {
        Connection conn = null;
        ConnectStudent connectStudent = new ConnectStudent();
        try {
            conn = connectStudent.connectionPool.reserveConnection();
            try (PreparedStatement statement = conn.prepareStatement("INSERT INTO StudentTable (surname, name, patronymic, numGroup, date) VALUES (?,?,?,?,?)")) {
                statement.setObject(1, surnameADD);
                statement.setObject(2, nameADD);
                statement.setObject(3, patronymicADD);
                statement.setObject(4, numGroupADD);
                statement.setObject(5, dateADD);
                statement.executeUpdate();
                statement.close();
            }
            conn.commit();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            connectStudent.connectionPool.releaseConnection(conn);
        }
    }

    public void Edit(int id, String surnameEdit, String nameEdit, String patronymicEdit, String numGroupEdit, String dateEdit) {
        Connection conn = null;
        ConnectStudent connectStudent = new ConnectStudent();
        try {
            conn = connectStudent.connectionPool.reserveConnection();
            try (PreparedStatement statement = conn.prepareStatement("UPDATE StudentTable SET surname=?, name=?, patronymic=?, numGroup=?, date=? WHERE id=?")) {
                statement.setObject(1, surnameEdit);
                statement.setObject(2, nameEdit);
                statement.setObject(3, patronymicEdit);
                statement.setObject(4, numGroupEdit);
                statement.setObject(5, dateEdit);
                statement.setObject(6, id);
                statement.executeUpdate();
                statement.close();
            }
            conn.commit();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            connectStudent.connectionPool.releaseConnection(conn);
        }
    }
}
