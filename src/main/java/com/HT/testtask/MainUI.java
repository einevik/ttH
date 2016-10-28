package com.HT.testtask;

import com.HT.testtask.DAO.StudensDAO;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Title;
import com.vaadin.data.util.sqlcontainer.RowId;
import com.vaadin.data.util.sqlcontainer.SQLContainer;
import com.vaadin.data.util.sqlcontainer.connection.JDBCConnectionPool;
import com.vaadin.data.util.sqlcontainer.connection.SimpleJDBCConnectionPool;
import com.vaadin.data.util.sqlcontainer.query.TableQuery;
import com.vaadin.server.VaadinRequest;
import com.vaadin.ui.*;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.themes.ValoTheme;
import com.HT.testtask.DAO.Connect;

import java.sql.SQLException;


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


//        delete.addClickListener(new Button.ClickListener() {
//            @Override
//            public void buttonClick(ClickEvent clickEvent) {
//                Object rowId = studentTable.getValue();
//                if (rowId != null) {
//                    try {
//                        sTable.buildContainer().removeItem(rowId);
//
//                    } catch (SQLException e) {
//                        e.printStackTrace();
//                    }
//                    Notification.show("Удалено", Type.TRAY_NOTIFICATION);
//                } else {
//                    Notification.show("Выберите студента", Type.TRAY_NOTIFICATION);
//                }
//            }
//        });

        delete.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(ClickEvent clickEvent) {
                Object rowId = studentTable.getValue();
                if (rowId != null) {
                    try {
                        SQLContainer deleteContainer = new SQLContainer(new TableQuery("StudentTable", connect.connectionPool ));
                        deleteContainer.removeItem(rowId);
                        deleteContainer.commit();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }

                    Notification.show("Удалено", Type.TRAY_NOTIFICATION);
                } else {
                    Notification.show("Выберите студента", Type.TRAY_NOTIFICATION);
                }
            }
        });


        vLayout.addComponent(studentTable);
        vLayout.addComponent(hLayout);
        hLayout.addComponent(add);
        hLayout.addComponent(edit);
        hLayout.addComponent(delete);
        setContent(vLayout);
    }

}
