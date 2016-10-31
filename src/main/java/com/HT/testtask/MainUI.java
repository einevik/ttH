package com.HT.testtask;

import com.HT.testtask.DAO.StudensDAO;
import com.HT.testtask.EditWindow;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Title;
import com.vaadin.data.Container;
import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.data.util.sqlcontainer.SQLContainer;
import com.vaadin.server.VaadinRequest;
import com.vaadin.ui.*;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.themes.ValoTheme;

import java.sql.Date;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

@Title("Main UI")
@Theme(ValoTheme.THEME_NAME)
public class MainUI extends UI {


    @Override
    protected void init(VaadinRequest request) {


        StudensDAO studensDAO = new StudensDAO();
//        EditWindow editSub = new EditWindow();
        AddWindow addSub = new AddWindow();


        VerticalLayout vLayout = new VerticalLayout();
        VerticalLayout labelLayout = new VerticalLayout();
        HorizontalLayout hLayout = new HorizontalLayout();
        HorizontalLayout findLayout = new HorizontalLayout();

        vLayout.setMargin(true);

        Table studentTable = new Table();
        try {
            studentTable.setContainerDataSource(studensDAO.buildContainer());
        } catch (SQLException e) {
            e.printStackTrace();
        }

        studentTable.setColumnCollapsingAllowed(true);
        studentTable.setColumnCollapsed("ID", true);
        studentTable.setColumnHeader("SURNAME", "Фамилия");
        studentTable.setColumnHeader("NAME", "Имя");
        studentTable.setColumnHeader("PATRONYMIC", "Отчество");
        studentTable.setColumnHeader("NUMGROUP", "Группа");
        studentTable.setColumnAlignment("NUMGROUP", Table.ALIGN_CENTER);
        studentTable.setColumnHeader("DATE", "Дата рождения");

        studentTable.setColumnWidth("SURNAME", 160);
        studentTable.setColumnWidth("NAME", 160);
        studentTable.setColumnWidth("PATRONYMIC", 160);
        studentTable.setColumnWidth("NUMGROUP", 80);
        studentTable.setColumnWidth("DATE", 130);

        studentTable.setPageLength(10);
        studentTable.setSelectable(true);
//        studentTable.setEditable(true);

        TextField findStudent = new TextField();
        findStudent.setInputPrompt("Фамилия...");
        findStudent.setWidth(String.valueOf(483));
        TextField findGroup = new TextField();
        findGroup.setInputPrompt("Группа...");
        findGroup.setWidth(String.valueOf(83));

        Label findLabel = new Label();
//        findLabel.setValue("Фильтр");

        DateFormat df = new SimpleDateFormat("dd.MM.yyyy"); // ("dd MMM yyyy")
        studentTable.addGeneratedColumn("DATE", new Table.ColumnGenerator() {
            @Override
            public Object generateCell(Table source, Object itemId, Object columnId) {
                Item item = source.getItem(itemId);
                Property<Date> prop = item.getItemProperty(columnId);
                Date date = (Date) prop.getValue();
                return new Label(df.format(date));
            }
        });

        Button add = new Button("Добавить");
        add.setWidth(String.valueOf(232));
        Button edit = new Button("Изменить");
        edit.setWidth(String.valueOf(232));
        Button delete = new Button("Удалить");
        delete.setWidth(String.valueOf(233));
        Button apply = new Button("Приминть");
        apply.setWidth(String.valueOf(130));

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
                    Date date = (Date) studentTable.getContainerProperty(rowId, "DATE").getValue();

                    EditWindow editSub = new EditWindow(id, surname, name, patronymic, numGroup, date);
//                    System.out.println(testSurname);

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

        vLayout.addComponent(labelLayout);
        labelLayout.addComponent(findLabel);
        vLayout.addComponent(findLayout);
        findLayout.addComponent(findStudent);
        findLayout.addComponent(findGroup);
        findLayout.addComponent(apply);
        vLayout.addComponent(studentTable);
        vLayout.addComponent(hLayout);
        hLayout.addComponent(add);
        hLayout.addComponent(edit);
        hLayout.addComponent(delete);
        setContent(vLayout);
    }

//    public String getTestSurname (){
//        System.out.println(testSurname+"  testSurname Get test");
//        return testSurname;
//    }
}
