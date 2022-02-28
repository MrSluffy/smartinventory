package com.smart.inventory.application.views.menu.sold;

import com.smart.inventory.application.data.entities.Item;
import com.smart.inventory.application.data.entities.SoldItem;
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
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.shared.Registration;

import javax.annotation.Nonnull;
import javax.persistence.Transient;
import java.util.List;

public class SoldItemForm extends FormLayout {

    Binder<SoldItem> binder = new BeanValidationBinder<>(SoldItem.class);

    TextField description = new TextField("Description");

    IntegerField quantity = new IntegerField("Quantity");
    NumberField purchaseAmount = new NumberField("Purchase Amount");


    ComboBox<Item> itemList = new ComboBox<>("Item");

    Button cancel = new Button("Cancel");

    Button add = new Button("Add");

    Button save = new Button("Save");


    @Transient
    private SoldItem soldItem;


    public SoldItemForm(List<Item> allItems) {
        addClassName("item-form");

        quantity.setHasControls(true);
        quantity.setMin(0);

        itemList.setItems(allItems);
        itemList.setItemLabelGenerator(Item::getItemName);

        purchaseAmount.setReadOnly(true);

        add(itemList, description, quantity, purchaseAmount, createButtonLayout());

    }

    private Component createButtonLayout() {
        cancel.addThemeVariants(ButtonVariant.LUMO_CONTRAST);

        add.addClickShortcut(Key.ENTER);
        save.addClickShortcut(Key.ENTER);
        cancel.addClickShortcut(Key.ESCAPE);

        add.addClickListener(event -> addNewSoldItem());
        save.addClickListener(event -> validateAndSave());
        add.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        cancel.addClickListener(event -> fireEvent(new SoldItemFormEvent.CloseEvent(this)));

        return new HorizontalLayout(add, save, cancel);
    }

    private void validateAndSave() {
        try {
            binder.writeBean(soldItem);
            fireEvent(new SoldItemFormEvent.SaveEvent(this, soldItem));
        } catch (ValidationException e) {
            e.printStackTrace();
        }
    }

    public SoldItem getSoldItem() {
        return soldItem;
    }

    private void addNewSoldItem() {
        try {
            binder.writeBean(soldItem);
            fireEvent(new SoldItemFormEvent.AddEvent(this, soldItem));
        } catch (ValidationException e) {
            e.printStackTrace();
        }
    }

    public void setSoldItem(@Nonnull SoldItem soldItem) {
        this.soldItem = soldItem;
        binder.readBean(soldItem);
        itemList.setValue(soldItem.getItem());
        description.setValue(soldItem.getDescription());
        quantity.setValue(soldItem.getQuantity());
        purchaseAmount.setValue(soldItem.getItem().getPrice() * quantity.getValue());

    }

    public static abstract class SoldItemFormEvent extends ComponentEvent<SoldItemForm> {


        private final SoldItem soldItem;

        public SoldItemFormEvent(SoldItemForm source, SoldItem soldItem) {
            super(source, false);
            this.soldItem = soldItem;
        }

        public SoldItem getSoldItem() {
            return soldItem;
        }


        public static class SaveEvent extends SoldItemFormEvent {
            SaveEvent(SoldItemForm source, SoldItem soldItem) {
                super(source, soldItem);
                if (source.isVisible()) {
                    Notification.show(source.itemList.getValue().getItemName() + " " +
                                            " successfully updated",
                                    5000, Notification.Position.TOP_CENTER)
                            .addThemeVariants(NotificationVariant.LUMO_SUCCESS);
                }
            }
        }

        public static class AddEvent extends SoldItemFormEvent {
            AddEvent(SoldItemForm source, SoldItem soldItem) {
                super(source, soldItem);
                if (source.isVisible()) {
                    Notification.show(source.itemList.getValue().getItemName() + " " +
                                            " successfully added",
                                    5000, Notification.Position.TOP_CENTER)
                            .addThemeVariants(NotificationVariant.LUMO_SUCCESS);
                }
            }
        }


        public static class CloseEvent extends SoldItemFormEvent {
            CloseEvent(SoldItemForm source) {
                super(source, null);
            }
        }
    }

    public <T extends ComponentEvent<?>> Registration addListener(Class<T> eventType,
                                                                  ComponentEventListener<T> listener) {
        return getEventBus().addListener(eventType, listener);
    }
}
