package com.HT.testtask;

import com.HT.testtask.DAO.StudensDAO;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Title;
import com.vaadin.data.util.sqlcontainer.SQLContainer;
import com.vaadin.server.VaadinRequest;
import com.vaadin.ui.*;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.themes.ValoTheme;

import java.sql.SQLData;
import java.sql.SQLException;


@Title("Main UI")
@Theme(ValoTheme.THEME_NAME)
public class MainUI extends UI {

    @Override
    protected void init(VaadinRequest request) {

        StudensDAO studensDAO = new StudensDAO();
        EditWindow editWindow = new EditWindow();

        VerticalLayout vLayout = new VerticalLayout();
        HorizontalLayout hLayout = new HorizontalLayout();
        vLayout.setMargin(true);

        Table studentTable = new Table();
        try {
            studentTable.setContainerDataSource(studensDAO.buildContainer());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        studentTable.setColumnHeader("SURNAME", "Фамилия");
        studentTable.setColumnHeader("NAME", "Имя");
        studentTable.setColumnHeader("PATRONYMIC", "Отчество");
        studentTable.setColumnHeader("NUMGROUP", "Группа");
        studentTable.setColumnAlignment("NUMGROUP", Table.ALIGN_CENTER);
        studentTable.setColumnHeader("DATE", "Дата рождения");

        studentTable.setPageLength(10);
        studentTable.setSelectable(true);
//        studentTable.setEditable(true);


//        DateFormat df = new SimpleDateFormat("dd.MM.yyyy"); // ("dd MMM yyyy")
//        studentTable.addGeneratedColumn("DATE", new Table.ColumnGenerator() {
//            @Override
//            public Object generateCell(Table source, Object itemId, Object columnId) {
//                Item item = source.getItem(itemId);
//                Property<Date> prop = item.getItemProperty(columnId);
//                Date date = (Date) prop.getValue();
//                return new Label(df.format(date));
//            }
//        });

        Button add = new Button("Добавить");
        Button edit = new Button("Изменить");
        Button delete = new Button("Удалить");

        AddWindow addSub = new AddWindow();
        add.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(ClickEvent clickEvent) {
                UI.getCurrent().addWindow(addSub);
            }
        });

        addSub.addCloseListener(new Window.CloseListener() {
            @Override
            public void windowClose(Window.CloseEvent closeEvent) {
                SQLContainer update = (SQLContainer) studentTable.getContainerDataSource();
                update.refresh();
            }
        });

        EditWindow editSub = new EditWindow();
        edit.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(ClickEvent clickEvent) {
                Object rowId = studentTable.getValue();
                if (rowId!=null) {
                    int id = (int)studentTable.getContainerProperty(rowId, "ID").getValue();
                    String surname = (String)studentTable.getContainerProperty(rowId, "SURNAME").getValue();
                    String name = (String)studentTable.getContainerProperty(rowId, "NAME").getValue();
                    String patronymic = (String)studentTable.getContainerProperty(rowId, "PATRONYMIC").getValue();
                    int numGroup = (int)studentTable.getContainerProperty(rowId, "NUMGROUP").getValue();
                    Object date = studentTable.getContainerProperty(rowId, "DATE").getValue();

                    editWindow.editFromMain(id, surname, name, patronymic, numGroup, date);

                    System.out.println(surname);
                    System.out.println(name);
                    System.out.println(patronymic);
                    System.out.println(numGroup);
                    System.out.println(date);
//                    studentTable.setValue(null);
                    UI.getCurrent().addWindow(editSub);
                } else {
                    Notification.show("Выберите студента", Type.TRAY_NOTIFICATION);
                }
            }
        });

        delete.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(ClickEvent clickEvent) {
                Object rowId = studentTable.getValue();
                if (rowId!=null) {
                    int id = (int)studentTable.getContainerProperty(rowId, "ID").getValue();
                    studensDAO.Delete(id);
                    SQLContainer update = (SQLContainer) studentTable.getContainerDataSource();
                    update.refresh();
                    studentTable.setValue(null);
                    Notification.show("Студент удален", Type.TRAY_NOTIFICATION);
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
