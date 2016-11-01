package com.HT.testtask;

import com.HT.testtask.DAO.GroupsDAO;
import com.HT.testtask.DAO.StudensDAO;
import com.vaadin.data.Property;
import com.vaadin.data.validator.RegexpValidator;
import com.vaadin.ui.*;
import com.vaadin.ui.Button.ClickEvent;

class AddWindowGroup extends Window {
    public AddWindowGroup() {
        super("Добавить факультет");
        center();

        MainUI mainUI = new MainUI();
        GroupsDAO studensDAO = new GroupsDAO();
        VerticalLayout vLayout = new VerticalLayout();
        HorizontalLayout hLayoutDate = new HorizontalLayout();
        HorizontalLayout hLayoutButton = new HorizontalLayout();
        HorizontalLayout clearLayout = new HorizontalLayout();
        VerticalLayout clearVertical1 = new VerticalLayout();
        VerticalLayout clearVertical2 = new VerticalLayout();
        VerticalLayout vLayoutLabel = new VerticalLayout();
        VerticalLayout vLayoutDate = new VerticalLayout();
        
        TextField nameFac = new TextField("Факультет");
        TextField numGroup = new TextField("Группа");

        clearVertical1.setWidth(String.valueOf(15));
        clearVertical2.setWidth(String.valueOf(15));
        
        nameFac.addValidator(new RegexpValidator("^[А-ЯЁа-яё]+$", "Кириллица без пробелов"));
        numGroup.addValidator(new RegexpValidator("\\d+", "Только цифры без пробелов"));


        vLayout.addComponent(nameFac);
        vLayout.addComponent(numGroup);
//        vLayout.addComponent(vLayoutDate);
//        vLayoutDate.addComponent(hLayoutDate);
        vLayout.addComponent(clearLayout);
        vLayout.addComponent(hLayoutButton);
        hLayoutDate.addComponent(clearVertical1);
        hLayoutDate.addComponent(clearVertical2);
        vLayout.setHeight("");
        vLayout.setWidth("");
        vLayout.setMargin(true);

        setModal(true);
        setClosable(false);
        setResizable(false);
        setContent(vLayout);

        Button ok = new Button("OK");
        hLayoutButton.addComponent(ok);
        Button cancel = new Button("Отменить");
        cancel.setWidth(String.valueOf(130));
        hLayoutButton.addComponent(cancel);
        
        ok.addClickListener(new Button.ClickListener() {
            public void buttonClick(ClickEvent event) {
                if (!nameFac.getValue().equals("")& !numGroup.getValue().equals("")& nameFac.isValid() == true& numGroup.isValid() == true) {
                    studensDAO.Add(nameFac.getValue().toString(),numGroup.getValue().toString());
                    nameFac.setValue("");
                    numGroup.setValue("");
                    Notification.show("Добавлен новый факультет", Notification.Type.TRAY_NOTIFICATION);
                    close(); // Close the sub-window
                } else {
                    Notification.show("Неверный формат или пустые поля", Notification.Type.TRAY_NOTIFICATION);
                }
            }
        });

        cancel.addClickListener(new Button.ClickListener() {
            public void buttonClick(ClickEvent event) {
                nameFac.setValue("");
                numGroup.setValue("");
                close();
            }
        });
    }
}
