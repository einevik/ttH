package com.HT.testtask;

import com.HT.testtask.DAO.StudensDAO;
import com.vaadin.data.validator.RegexpValidator;
import com.vaadin.ui.*;
import com.vaadin.ui.Button.ClickEvent;

class AddWindow extends Window {
    public AddWindow() {
        super("Добавить студента");
        center();

        MainUI mainUI = new MainUI();
        StudensDAO studensDAO = new StudensDAO();
        VerticalLayout vLayout = new VerticalLayout();
        HorizontalLayout hLayout = new HorizontalLayout();
        HorizontalLayout clearLayout = new HorizontalLayout();

        TextField surname = new TextField("Фамилия");
        TextField name = new TextField("Имя");
        TextField patronymic = new TextField("Отчество");
        TextField numGroup = new TextField("Группа");
        TextField date = new TextField("Дата Рождения");
        TextField

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
            public void buttonClick(ClickEvent event) {
                if (!surname.getValue().equals("")
                        & !name.getValue().equals("")
                        & !patronymic.getValue().equals("")
                        & !numGroup.getValue().equals("")
                        & surname.isValid()==true
                        & name.isValid()==true
                        & patronymic.isValid()==true
                        & numGroup.isValid()==true
                        & date.isValid()==true)
                {

                    studensDAO.Add(surname.getValue().toString(),
                            name.getValue().toString(),
                            patronymic.getValue().toString(),
                            numGroup.getValue().toString(),
                            date.getValue().toString());

                    Notification.show("Добавлен новый студент", Notification.Type.TRAY_NOTIFICATION);
                    close(); // Close the sub-window
                } else {
                    Notification.show("Неверный формат или пустые поля", Notification.Type.TRAY_NOTIFICATION);
                }
            }
        });
        hLayout.addComponent(ok);

        Button cancel = new Button("Отменить");
        cancel.setWidth(String.valueOf(130));
        cancel.addClickListener(new Button.ClickListener() {
            public void buttonClick(ClickEvent event) {
                close();
            }
        });
        hLayout.addComponent(cancel);

    }
}
