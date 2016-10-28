package com.HT.testtask;


import com.vaadin.data.Validator;
import com.vaadin.data.validator.RegexpValidator;
import com.vaadin.ui.*;



public class EditWindow extends Window {
    public EditWindow() {
        super("Изменить данные"); // Set window caption
        center();

        // Some basic vLayout for the window
        VerticalLayout vLayout = new VerticalLayout();
        HorizontalLayout hLayout = new HorizontalLayout();
        HorizontalLayout clearLayout = new HorizontalLayout();

        TextField surname = new TextField("Фамилия");
        TextField name = new TextField("Имя");
        TextField patronymic = new TextField("Отчество");
        TextField numGroup = new TextField("Группа");
        TextField date = new TextField("Дата Рождения");

        surname.addValidator(new RegexpValidator("^[А-ЯЁа-яё]+$", "Только буквы без пробелов"));
        name.addValidator(new RegexpValidator("^[А-ЯЁа-яё]+$", "Только буквы без пробелов"));
        patronymic.addValidator(new RegexpValidator("^[А-ЯЁа-яё]+$", "Только буквы без пробелов"));
        numGroup.addValidator(new RegexpValidator("\\d+", "Только цифры без пробелов"));
        date.addValidator(new RegexpValidator("(0[1-9]|[12][0-9]|3[01])[- /.](0[1-9]|1[012])[- /.](19|20)\\d\\d", "Разделители: . / -"));
        date.setInputPrompt("дд.мм.гггг");

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
        ok.addClickListener(new Button.ClickListener() {
            public void buttonClick(Button.ClickEvent event) {
                close(); // Close the sub-window
            }
        });
        hLayout.addComponent(ok);

        Button cancel = new Button("Отменить");
        cancel.setWidth(String.valueOf(130));
        cancel.addClickListener(new Button.ClickListener() {
            public void buttonClick(Button.ClickEvent event) {
                close(); // Close the sub-window
            }
        });
        hLayout.addComponent(cancel);

    }
}
