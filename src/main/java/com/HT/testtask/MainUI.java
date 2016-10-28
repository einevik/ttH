package com.HT.testtask;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Title;
import com.vaadin.server.VaadinRequest;
import com.vaadin.ui.*;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.themes.ValoTheme;

import com.HT.testtask.DAO.StudensDAO;

import java.sql.SQLException;


@Title("Main UI")
@Theme(ValoTheme.THEME_NAME)
public class MainUI extends UI {

    @Override
    protected void init(VaadinRequest request) {

        //StudensDAO sTable = new StudensDAO();
        VerticalLayout vLayout = new VerticalLayout();
        HorizontalLayout hLayout = new HorizontalLayout();
        vLayout.setMargin(true);

        StudensDAO sTable = new StudensDAO();
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

        delete.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(ClickEvent clickEvent) {
                Object rowId = studentTable.getValue();
                if (rowId != null) {

                    SQLContainer deleteContainer = new SQLContainer(new TableQuery("tbl_grade", connectionPool));
                    RowId itemID = new RowId(new Integer[] { 10 });
                    deleteContainer.removeItem(itemID);
                    deleteContainer.commit();

                    Notification.show("Выбран", Type.TRAY_NOTIFICATION);
                }
                else {
                    Notification.show("Не выбран", Type.TRAY_NOTIFICATION);
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
