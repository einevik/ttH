package com.HT.testtask;

import com.vaadin.data.validator.RegexpValidator;
import com.vaadin.ui.*;
import com.vaadin.ui.Button.ClickEvent;


class AddWindow extends Window {
    public AddWindow() {
        super("Добавить"); // Set window caption
        center();

        // Some basic vLayout for the window
        VerticalLayout vLayout = new VerticalLayout();
        HorizontalLayout hLayout = new HorizontalLayout();
        HorizontalLayout clearLayout = new HorizontalLayout();

        TextField surname = new TextField("Фамилия");
        surname.addValidator(new RegexpValidator("^[А-ЯЁа-яё]+$", "Только буквы без пробелов"));
        TextField name = new TextField("Имя");
        name.addValidator(new RegexpValidator("^[А-ЯЁа-яё]+$", "Только буквы без пробелов"));
        TextField patronymic = new TextField("Отчество");
        patronymic.addValidator(new RegexpValidator("^[А-ЯЁа-яё]+$", "Только буквы без пробелов"));
        TextField numGroup = new TextField("Группа");
        numGroup.addValidator(new RegexpValidator("\\d+", "Только цифры без пробелов"));
        TextField date = new TextField("Дата Рождения");

        vLayout.addComponent(surname);
        vLayout.addComponent(name);
        vLayout.addComponent(patronymic);
        vLayout.addComponent(numGroup);
        vLayout.addComponent(date);
        vLayout.addComponent(clearLayout);
        vLayout.addComponent(hLayout);

        vLayout.setHeight("");
        vLayout.setWidth("");
        vLayout.setMargin(true);

        setModal(true);
        setClosable(false);
        setResizable(false);
        setContent(vLayout);

        Button ok = new Button("OK");
        //ok.setWidth(100.0f, Unit.PERCENTAGE);
        ok.addClickListener(new Button.ClickListener() {
            public void buttonClick(ClickEvent event) {
                close(); // Close the sub-window
            }
        });
        hLayout.addComponent(ok);

        Button cancel = new Button("Отменить");
        //cancel.setWidth(100.0f, Unit.PERCENTAGE);
        cancel.addClickListener(new Button.ClickListener() {
            public void buttonClick(ClickEvent event) {
                close(); // Close the sub-window
            }
        });
        hLayout.addComponent(cancel);

    }
}
