package com.HT.testtask;

import com.HT.testtask.DAO.Connect;
import com.HT.testtask.DAO.StudensDAO;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Title;
import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.data.util.sqlcontainer.SQLContainer;
import com.vaadin.server.VaadinRequest;
import com.vaadin.ui.*;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.themes.ValoTheme;

import java.sql.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;


@Title("Main UI")
@Theme(ValoTheme.THEME_NAME)
public class MainUI extends UI {

    @Override
    protected void init(VaadinRequest request) {

        StudensDAO sTable = new StudensDAO();
        Connect connect = new Connect();

        VerticalLayout vLayout = new VerticalLayout();
        HorizontalLayout hLayout = new HorizontalLayout();
        vLayout.setMargin(true);

        Table studentTable = new Table();
        try {
            studentTable.setContainerDataSource(sTable.buildContainer());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        studentTable.setColumnHeader("SURNAME", "Фамилия");
        studentTable.setColumnHeader("NAME", "Имя");
        studentTable.setColumnHeader("PATRONYMIC", "Отчество");
        studentTable.setColumnHeader("NUMGROUP", "Группа");
        studentTable.setColumnAlignment ( "NUMGROUP",Table.ALIGN_CENTER);
        studentTable.setColumnHeader("DATE", "Дата рождения");

        studentTable.setPageLength(10);
        studentTable.setSelectable(true);
//        studentTable.setEditable(true);

        DateFormat df = new SimpleDateFormat("dd.MM.yyyy"); // ("dd MMM yyyy")
        studentTable.addGeneratedColumn("DATE", new Table.ColumnGenerator() {

            @Override
            public Object generateCell(Table source, Object itemId,
                                       Object columnId) {

                Item item = source.getItem(itemId);

                Property<Date> prop = item.getItemProperty(columnId);

                Date date = (Date) prop.getValue();

                return new Label(df.format(date));
            }
        });

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


        delete.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(ClickEvent clickEvent) {
                Object rowId = studentTable.getValue();
                if (rowId != null) {
                    Connection conn = null;
                    try {
                        conn = connect.connectionPool.reserveConnection();
                        try (PreparedStatement statement = conn.prepareStatement("DELETE FROM StudentTable WHERE ID = ?")) {
                            statement.setInt(1,2);
                            statement.executeUpdate();
                            statement.close();
                        }
                        conn.commit();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    } finally {
                        connect.connectionPool.releaseConnection(conn);
                    }

                    SQLContainer update =  (SQLContainer)studentTable.getContainerDataSource();
                    update.refresh();

                    Notification.show("Студент удален", Type.TRAY_NOTIFICATION);
                } else {
                    Notification.show("Выберите студента", Type.TRAY_NOTIFICATION);
                }
            }
        });

//        delete.addClickListener(new Button.ClickListener() {
//            @Override
//            public void buttonClick(ClickEvent clickEvent) {
//                Object rowId = studentTable.getValue();
//                if (rowId != null) {
//                    try {
//                        SQLContainer container = new SQLContainer(new TableQuery("StudentTable", connect.connectionPool));
//                        container.removeItem("ID");
//                        container.commit();
//                    } catch (SQLException e) {
//                        e.printStackTrace();
//                    }
//                    Notification.show("Удалено", Type.TRAY_NOTIFICATION);
//                } else {
//                    Notification.show("Выберите студента", Type.TRAY_NOTIFICATION);
//                }
//            }
//        });

        vLayout.addComponent(studentTable);
        vLayout.addComponent(hLayout);
        hLayout.addComponent(add);
        hLayout.addComponent(edit);
        hLayout.addComponent(delete);
        setContent(vLayout);
    }

}
