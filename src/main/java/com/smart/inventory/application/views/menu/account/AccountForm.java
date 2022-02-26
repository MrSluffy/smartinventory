package com.smart.inventory.application.views.menu.account;

import com.smart.inventory.application.data.entity.Employer;
import com.smart.inventory.application.data.entity.Position;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.shared.Registration;

import java.util.List;

public class AccountForm extends FormLayout {

    Binder<Employer> employerBinder = new BeanValidationBinder<>(Employer.class);

    public TextField firstName = new TextField("First Name");
    public TextField lastName = new TextField("Last Name");
    public TextField email = new TextField("Email");

    public PasswordField passwordField = new PasswordField("Password");

    ComboBox<Position> position = new ComboBox<>("Status");


    Button cancel = new Button("Cancel");

    Button add = new Button("Add");

    Button save = new Button("Save");



    private Employer employer;

    public AccountForm(List<Position> positions){
        addClassName("item-form");
        employerBinder.bindInstanceFields(this);

        position.setItems(positions);
        position.setItemLabelGenerator(Position::getPostionName);

        add(firstName, lastName, email, passwordField, position, createButtonLayout());


    }

    private Component createButtonLayout() {

        cancel.addThemeVariants(ButtonVariant.LUMO_CONTRAST);

        add.addClickShortcut(Key.ENTER);
        save.addClickShortcut(Key.ENTER);
        cancel.addClickShortcut(Key.ESCAPE);

        add.addClickListener(event -> addNewEmployer());
        save.addClickListener(event -> validateAndSave());
        add.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        cancel.addClickListener(event -> fireEvent(new AccountFormEvent.CloseEvent(this)));

        return new HorizontalLayout(add, save, cancel);
    }

    private void addNewEmployer() {
        try {
            employerBinder.writeBean(employer);
            fireEvent(new AccountFormEvent.AddEvent(this, employer));
        } catch (ValidationException e) {
            e.printStackTrace();
        }
    }

    private void validateAndSave() {
        try {
            employerBinder.writeBean(employer);
            fireEvent(new AccountFormEvent.SaveEvent(this, employer));
        } catch (ValidationException e) {
            e.printStackTrace();
        }
    }


    public Employer getEmployer(){
        return employer;
    }

    public void setEmployerNew(Employer employer) {
        this.employer = employer;
    }

    public static abstract class AccountFormEvent extends ComponentEvent<AccountForm> {

        private final Employer employer;

        public AccountFormEvent(AccountForm source, Employer employer) {
            super(source, false);
            this.employer = employer;
        }


        public Employer getEmployer(){
            return employer;
        }

        public static class AddEvent extends AccountFormEvent {
            AddEvent(AccountForm source, Employer employer) {
                super(source, employer);
                if (source.isVisible()) {
                    Notification.show( source.email.getValue() + " successfully added",
                                    5000, Notification.Position.TOP_CENTER)
                            .addThemeVariants(NotificationVariant.LUMO_SUCCESS);
                }
            }
        }

        public static class SaveEvent extends AccountFormEvent {
            SaveEvent(AccountForm source, Employer employer) {
                super(source, employer);
                if (source.isVisible()) {
                    Notification.show( source.email.getValue() + " successfully updated",
                                    5000, Notification.Position.TOP_CENTER)
                            .addThemeVariants(NotificationVariant.LUMO_SUCCESS);
                }
            }
        }

        public static class CloseEvent extends AccountFormEvent {
            CloseEvent(AccountForm source) {
                super(source, null);
            }
        }
    }

    public <T extends ComponentEvent<?>> Registration addListener(Class<T> eventType,
                                                                  ComponentEventListener<T> listener) {
        return getEventBus().addListener(eventType, listener);
    }
}
