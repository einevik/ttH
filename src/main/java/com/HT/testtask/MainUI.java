package com.HT.testtask;

import com.HT.testtask.DAO.GroupsDAO;
import com.HT.testtask.DAO.StudensDAO;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Title;
import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.data.util.sqlcontainer.SQLContainer;
import com.vaadin.data.validator.RegexpValidator;
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
        GroupsDAO groupsDAO = new GroupsDAO();
        AddWindow addSub = new AddWindow();

        VerticalLayout vLayout = new VerticalLayout();
        HorizontalLayout hLayout = new HorizontalLayout();
        HorizontalLayout findLayout = new HorizontalLayout();
        VerticalLayout groupVLayout = new VerticalLayout();
        VerticalLayout groupVClear = new VerticalLayout();
        HorizontalLayout groupHLayout = new HorizontalLayout();
        vLayout.setMargin(true);

        TextField findStudent = new TextField();
        findStudent.addValidator(new RegexpValidator("^[А-ЯЁа-яё]+$", "Кириллица без пробелов"));
        findStudent.setInputPrompt("Фамилия...");
        findStudent.setWidth(String.valueOf(484));
        TextField findGroup = new TextField();
        findGroup.addValidator(new RegexpValidator("\\d+", "Только цифры без пробелов"));
        findGroup.setInputPrompt("Группа...");
        findGroup.setWidth(String.valueOf(82));
        Button add = new Button("Добавить");
        add.setWidth(String.valueOf(232));
        Button edit = new Button("Изменить");
        edit.setWidth(String.valueOf(232));
        Button delete = new Button("Удалить");
        delete.setWidth(String.valueOf(233));
        Button apply = new Button("Приминть");
        apply.setWidth(String.valueOf(130));

        Button addGroup = new Button("Добавить");
        addGroup.setWidth(String.valueOf(232));
        Button editGroup = new Button("Изменить");
        editGroup.setWidth(String.valueOf(232));
        Button deleteGroup = new Button("Удалить");
        deleteGroup.setWidth(String.valueOf(233));

        String mainQuery = "SELECT * FROM StudentTable";
        String groupQuery = "SELECT * FROM GroupTable";
//        String filterAll = "SELECT * FROM StudentTable WHERE surname='"+filterGroup+"'";
        Table studentTable = new Table();
        if (findStudent.getValue().equals("") && findGroup.getValue().equals("")) {
            try {
                studentTable.setContainerDataSource(studensDAO.buildContainer(mainQuery));
            } catch (SQLException e) {
                e.printStackTrace();
            }
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


        Table groupTable = new Table();
            try {
                groupTable.setContainerDataSource(groupsDAO.buildContainer(groupQuery));
            } catch (SQLException e) {
                e.printStackTrace();
            }
        groupTable.setColumnCollapsingAllowed(true);
        groupTable.setColumnCollapsed("ID", true);
        groupTable.setColumnHeader("NAMEFAC", "Факультет");
        groupTable.setColumnHeader("NUMGROUP", "Группа");
        groupTable.setColumnAlignment("NUMGROUP", Table.ALIGN_CENTER);
        groupTable.setColumnWidth("NAMEFAC", 600);
        groupTable.setColumnWidth("NUMGROUP", 90);
        groupTable.setPageLength(5);
        groupTable.setSelectable(true);


        vLayout.addComponent(findLayout);
        findLayout.addComponent(findStudent);
        findLayout.addComponent(findGroup);
        findLayout.addComponent(apply);
        vLayout.addComponent(studentTable);
        vLayout.addComponent(hLayout);
        hLayout.addComponent(add);
        hLayout.addComponent(edit);
        hLayout.addComponent(delete);
        vLayout.addComponent(groupVClear);
        vLayout.addComponent(groupVLayout);
        groupVLayout.addComponent(groupTable);
        vLayout.addComponent(groupHLayout);
        groupHLayout.addComponent(addGroup);
        groupHLayout.addComponent(editGroup);
        groupHLayout.addComponent(deleteGroup);
        setContent(vLayout);

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
                if (rowId != null) {
                    int id = (int) studentTable.getContainerProperty(rowId, "ID").getValue();
                    String surname = (String) studentTable.getContainerProperty(rowId, "SURNAME").getValue();
                    String name = (String) studentTable.getContainerProperty(rowId, "NAME").getValue();
                    String patronymic = (String) studentTable.getContainerProperty(rowId, "PATRONYMIC").getValue();
                    int numGroup = (int) studentTable.getContainerProperty(rowId, "NUMGROUP").getValue();
                    Date date = (Date) studentTable.getContainerProperty(rowId, "DATE").getValue();

                    EditWindow editSub = new EditWindow(id, surname, name, patronymic, numGroup, date);

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
                if (rowId != null) {
                    int id = (int) studentTable.getContainerProperty(rowId, "ID").getValue();
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

        apply.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(ClickEvent clickEvent) {
                String filterSurname, filterGroup;
                filterSurname = findStudent.getValue();
                filterGroup = findGroup.getValue();
                String filterSurnameQuery = "SELECT * FROM StudentTable WHERE surname='" + filterSurname + "'";
                String filterGroupQuery = "SELECT * FROM StudentTable WHERE numGroup='" + filterGroup + "'";
                String filterAll = "SELECT * FROM StudentTable WHERE surname='" + filterSurname + "'" + " AND numGroup='" + filterGroup + "'";
                if (!filterSurname.equals("") && filterGroup.equals("")) {
                    try {
                        studentTable.setContainerDataSource(studensDAO.buildContainer(filterSurnameQuery));
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    SQLContainer update = (SQLContainer) studentTable.getContainerDataSource();
                    update.refresh();
//                    System.out.println(filterSurnameQuery);
                } else if (filterSurname.equals("") && !filterGroup.equals("")) {
                    try {
                        studentTable.setContainerDataSource(studensDAO.buildContainer(filterGroupQuery));
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    SQLContainer update = (SQLContainer) studentTable.getContainerDataSource();
                    update.refresh();
//                    System.out.println(filterGroupQuery);
                } else if (!filterSurname.equals("") && !filterGroup.equals("")) {
                    try {
                        studentTable.setContainerDataSource(studensDAO.buildContainer(filterAll));
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    SQLContainer update = (SQLContainer) studentTable.getContainerDataSource();
                    update.refresh();
//                    System.out.println(filterAll);
                } else {
                    try {
                        studentTable.setContainerDataSource(studensDAO.buildContainer(mainQuery));
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    SQLContainer update = (SQLContainer) studentTable.getContainerDataSource();
                    update.refresh();
                }
                studentTable.setColumnCollapsed("ID", true);
            }
        });


        addGroup.addClickListener(new Button.ClickListener() {
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
    }
}
