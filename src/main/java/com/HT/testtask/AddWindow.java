package com.HT.testtask;

import com.HT.testtask.DAO.StudensDAO;
import com.vaadin.data.Property;
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
        HorizontalLayout hLayoutDate = new HorizontalLayout();
        HorizontalLayout hLayoutButton = new HorizontalLayout();
        HorizontalLayout clearLayout = new HorizontalLayout();
        VerticalLayout clearVertical1 = new VerticalLayout();
        VerticalLayout clearVertical2 = new VerticalLayout();
        VerticalLayout vLayoutLabel = new VerticalLayout();
        VerticalLayout vLayoutDate = new VerticalLayout();

        Label labelDate = new Label("Дата рождения");
        TextField surname = new TextField("Фамилия");
        TextField name = new TextField("Имя");
        TextField patronymic = new TextField("Отчество");
        TextField numGroup = new TextField("Группа");

        clearVertical1.setWidth(String.valueOf(15));
        clearVertical2.setWidth(String.valueOf(15));
//        vLayoutLabel.setHeight(String.valueOf(15));
//        vLayoutDate.setHeight(String.valueOf(10));
//        clearLayout.setHeight(String.valueOf(55));

        TextField day = new TextField("");
        day.setWidth(String.valueOf(42));
        day.setInputPrompt("дд");

        TextField month = new TextField("");
        month.setWidth(String.valueOf(43));
        month.setInputPrompt("мм");

        TextField year = new TextField("");
        year.setWidth(String.valueOf(70));
        year.setInputPrompt("гггг");

        surname.addValidator(new RegexpValidator("^[А-ЯЁа-яё]+$", "Только буквы без пробелов"));
        name.addValidator(new RegexpValidator("^[А-ЯЁа-яё]+$", "Только буквы без пробелов"));
        patronymic.addValidator(new RegexpValidator("^[А-ЯЁа-яё]+$", "Только буквы без пробелов"));
        numGroup.addValidator(new RegexpValidator("\\d+", "Только цифры без пробелов"));

//        if (month.getValue().equals("01")||month.getValue().equals("03")||month.getValue().equals("05")||month.getValue().equals("07")
//                ||month.getValue().equals("08")||month.getValue().equals("10")||month.getValue().equals("12")||month.getValue().equals("")){
//            day.addValidator(new RegexpValidator("(0[1-9]|[10-31])", "с 01 до 31"));System.out.println("if"+month.getValue());
//        } else if (!month.getValue().equals("02")){System.out.println("els if");
//            day.addValidator(new RegexpValidator("(0[1-9]|[10-30])", "с 01 до 30"));
//        }
//        day.addValidator(new RegexpValidator("(0[1-9]|[12][0-9]|3[01])", "с 01 до 31"));
        month.addValidator(new RegexpValidator("(0[1-9]|1[012])", "с 01 до 12"));
        year.addValidator(new RegexpValidator("(19|20)\\d\\d", "с 1900 до 2099"));


        month.addListener(new Property.ValueChangeListener() {
            public void valueChange(Property.ValueChangeEvent event) {
                if (month.getValue().equals("01") || month.getValue().equals("03") || month.getValue().equals("05") || month.getValue().equals("07")
                        || month.getValue().equals("08") || month.getValue().equals("10") || month.getValue().equals("12") || month.getValue().equals("")) {
                    day.removeAllValidators();
                    day.addValidator(new RegexpValidator("(0[1-9]|[12][0-9]|3[01])", "с 01 до 31"));
                    System.out.println("if" + month.getValue());
                } else if (!month.getValue().equals("02")) {
                    System.out.println("els if");
                    day.removeAllValidators();
                    day.addValidator(new RegexpValidator("(0[1-9]|[12][0-9]|3[0])", "с 01 до 30"));
                }System.out.println(month.getValue());
            }
        });

        vLayout.addComponent(surname);
        vLayout.addComponent(name);
        vLayout.addComponent(patronymic);
        vLayout.addComponent(numGroup);
        vLayout.addComponent(vLayoutDate);
        vLayoutDate.addComponent(labelDate);
        vLayoutDate.addComponent(hLayoutDate);

        vLayout.addComponent(clearLayout);
        vLayout.addComponent(hLayoutButton);

        hLayoutDate.addComponent(day);
        hLayoutDate.addComponent(clearVertical1);
        hLayoutDate.addComponent(month);
        hLayoutDate.addComponent(clearVertical2);
        hLayoutDate.addComponent(year);

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
                        & !day.getValue().equals("")
                        & !month.getValue().equals("")
                        & !year.getValue().equals("")
                        & surname.isValid()==true
                        & name.isValid()==true
                        & patronymic.isValid()==true
                        & numGroup.isValid()==true
                        & day.isValid()==true
                        & month.isValid()==true
                        & year.isValid()==true)
                {
                    String date = year.getValue()+"-"+ month.getValue()+"-"+day.getValue();
                    studensDAO.Add
                            (surname.getValue().toString(),
                            name.getValue().toString(),
                            patronymic.getValue().toString(),
                            numGroup.getValue().toString(),
                            date);

                    surname.setValue("");
                    name.setValue("");
                    patronymic.setValue("");
                    numGroup.setValue("");
                    year.setValue("");
                    month.setValue("");
                    day.setValue("");

                    Notification.show("Добавлен новый студент", Notification.Type.TRAY_NOTIFICATION);
                    close(); // Close the sub-window
                } else {
                    Notification.show("Неверный формат или пустые поля", Notification.Type.TRAY_NOTIFICATION);
                }
            }
        });
        hLayoutButton.addComponent(ok);

        Button cancel = new Button("Отменить");
        cancel.setWidth(String.valueOf(130));
        cancel.addClickListener(new Button.ClickListener() {
            public void buttonClick(ClickEvent event) {
                close();
            }
        });
        hLayoutButton.addComponent(cancel);

    }
}
