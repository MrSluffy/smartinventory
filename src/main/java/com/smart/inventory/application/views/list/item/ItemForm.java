package com.smart.inventory.application.views.list.item;

import com.smart.inventory.application.data.entity.Item;
import com.smart.inventory.application.views.extension.CusComponent;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.shared.Registration;

import javax.persistence.Transient;

public class ItemForm extends FormLayout {
    Binder<Item> itemBinder = new BeanValidationBinder<>(Item.class);

    @Transient
    private Item item;


    TextField itemName = new TextField("Item Name");
    IntegerField quantity = new IntegerField("Quantity");
    NumberField price = new NumberField("Price");
    NumberField totalPrice = new NumberField("Total Price");
    Select<String> selectCurrency = new Select<>();

    Button save = new Button("Save");
    Button cancel = new Button("Cancel");

    Div currencyPrefix = new Div();
    Div currencyPrefix1 = new Div();


    public Item getItem() {
        return item;
    }


    public ItemForm() {
        addClassName("item-form");

        setHeightFull();

        quantity.setHasControls(true);
        quantity.setMin(0);
        quantity.setMax(Integer.MAX_VALUE);

        itemBinder.bindInstanceFields(this);

        totalPrice.setReadOnly(true);

        add(itemName,
                quantity,
                createPriceLayout(),
                totalPrice,
                createButtonLayout());
    }


    private Component createPriceLayout() {
        return CusComponent.getComponent(selectCurrency, currencyPrefix, currencyPrefix1, price, totalPrice);
    }

    public void setItem(Item item) {
        this.item = item;
        itemBinder.readBean(item);
        quantity.setValue(item.getQuantity());
        totalPrice.setValue(item.getPrice() * item.getQuantity());
    }

    private Component createButtonLayout() {
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        cancel.addThemeVariants(ButtonVariant.LUMO_CONTRAST);

        save.addClickShortcut(Key.ENTER);
        cancel.addClickShortcut(Key.ESCAPE);

        save.addClickListener(event -> validateAndSave());
        cancel.addClickListener(event -> fireEvent(new ItemFormEvent.CloseEvent(this)));

        itemBinder.addStatusChangeListener(e -> save.setEnabled(itemBinder.isValid()));
        return new HorizontalLayout(save, cancel);
    }

    private void validateAndSave() {
        try {
            itemBinder.writeBean(item);
            fireEvent(new ItemFormEvent.SaveEvent(this, item));
        } catch (ValidationException e) {
            e.printStackTrace();
        }
    }


    public static abstract class ItemFormEvent extends ComponentEvent<ItemForm> {

        private final Item item;

        public ItemFormEvent(ItemForm source, Item item) {
            super(source, false);
            this.item = item;
        }

        public Item getItem() {
            return item;
        }

        public static class SaveEvent extends ItemFormEvent {
            SaveEvent(ItemForm source, Item item) {
                super(source, item);
                source.currencyPrefix.setText(source.selectCurrency.getValue());
                source.currencyPrefix1.setText(source.selectCurrency.getValue());
                if (source.isVisible()) {
                    item.setTotalPrice(item.getQuantity());
                    Notification.show(source.itemName.getValue() + " " +
                                            " successfully updated",
                                    5000, Notification.Position.TOP_CENTER)
                            .addThemeVariants(NotificationVariant.LUMO_SUCCESS);
                }
            }
        }

        public static class CloseEvent extends ItemFormEvent {
            CloseEvent(ItemForm source) {
                super(source, null);
            }
        }
    }

    public <T extends ComponentEvent<?>> Registration addListener(Class<T> eventType,
                                                                  ComponentEventListener<T> listener) {
        return getEventBus().addListener(eventType, listener);
    }

}
