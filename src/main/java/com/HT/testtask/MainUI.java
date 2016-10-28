package com.HT.testtask;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Title;
import com.vaadin.data.Container;
import com.vaadin.data.util.sqlcontainer.SQLContainer;
import com.vaadin.data.util.sqlcontainer.connection.JDBCConnectionPool;
import com.vaadin.data.util.sqlcontainer.connection.SimpleJDBCConnectionPool;
import com.vaadin.data.util.sqlcontainer.query.FreeformQuery;
import com.vaadin.server.VaadinRequest;
import com.vaadin.ui.*;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.themes.ValoTheme;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.logging.Level;
import java.util.logging.Logger;


@Title("Main UI")
@Theme(ValoTheme.THEME_NAME)
public class MainUI extends UI {

    @Override
    protected void init(VaadinRequest request) {
//        GridLayout grid = new GridLayout(4,4);
//        grid.addStyleName("example-gridlayout");
        VerticalLayout vLayout = new VerticalLayout();
        HorizontalLayout hLayout = new HorizontalLayout();
        vLayout.setMargin(true);

        Table studentTable = new Table();
        try {
            studentTable.setContainerDataSource(buildContainer());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        studentTable.setColumnHeader("SURNAME", "Фамилия");
        studentTable.setColumnHeader("NAME", "Имя");
        studentTable.setColumnHeader("PATRONYMIC", "Отчество");
        studentTable.setColumnHeader("NUMGROUP", "Группа");
        studentTable.setColumnHeader("DATE", "Дата рождения");
//        studentTable.addContainerProperty("DATE",SimpleDateFormat.class, "yyyy-MM-dd");

        studentTable.setPageLength(10);
        studentTable.setSelectable(true);

        Button add = new Button("Добавить");
        Button edit = new Button("Изменить");
        Button delete = new Button("Удалить");

        add.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(ClickEvent clickEvent) {
                AddWindow addSub = new AddWindow();
                UI.getCurrent().addWindow(addSub);
            }
        });


        edit.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(ClickEvent clickEvent) {
                EditWindow editSub = new EditWindow();
                UI.getCurrent().addWindow(editSub);
            }
        });
//        delete.setWidth(100.0f, Unit.PERCENTAGE);
//        buttonADD.setWidth(100.0f, Unit.PERCENTAGE);

        vLayout.addComponent(studentTable);
        vLayout.addComponent(hLayout);
        hLayout.addComponent(add);
        hLayout.addComponent(edit);
        hLayout.addComponent(delete);
        setContent(vLayout);
    }

//    public Table studentTable() throws SQLException {


//        return table;
//    }

    private Container buildContainer() throws SQLException {
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
            statement.close();
            conn.commit();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            connectionPool.releaseConnection(conn);
        }
    }


    public void delete(Button.ClickEvent event) {
        Notification.show("Ничего не произошло", Type.TRAY_NOTIFICATION);

        //getUI().contactList.select(null);
    }

}
