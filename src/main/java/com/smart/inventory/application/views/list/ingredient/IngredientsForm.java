package com.smart.inventory.application.views.list.ingredient;

import com.smart.inventory.application.data.entity.ingredients.Ingredients;
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

import javax.annotation.Nonnull;

public class IngredientsForm extends FormLayout {
    Binder<Ingredients> ingredientsBinder = new BeanValidationBinder<>(Ingredients.class);

    private Ingredients ingredients;

    TextField productName = new TextField("Product Name");
    IntegerField productQuantity = new IntegerField("Product Quantity");
    NumberField productPrice = new NumberField("Product Price");
    NumberField totalCost = new NumberField("Total Cost");
    Select<String> selectCurrency = new Select<>();


    Button save = new Button("Save");
    Button cancel = new Button("Cancel");

    Div currencyPrefix = new Div();
    Div currencyPrefix1 = new Div();


    public Ingredients getCosting() {
        return ingredients;
    }


    public IngredientsForm() {
        addClassName("item-form");

        productQuantity.setHasControls(true);
        productQuantity.setMin(0);
        productQuantity.setMax(Integer.MAX_VALUE);

        ingredientsBinder.bindInstanceFields(this);


        totalCost.setReadOnly(true);

        add(productName,
                createQuantityLayout(),
                createPriceLayout(),
                totalCost,
                createButtonLayout());
    }

    @Nonnull
    private Component createQuantityLayout() {
        return new HorizontalLayout(productQuantity);
    }


    @Nonnull
    private Component createPriceLayout() {
        return CusComponent.getComponent(selectCurrency, currencyPrefix, currencyPrefix1, productPrice, totalCost);
    }



    public void setCosting(Ingredients ingredients) {
        this.ingredients = ingredients;
        ingredientsBinder.readBean(ingredients);
        totalCost.setValue(ingredients.getProductPrice() * ingredients.getProductQuantity());
    }

    private Component createButtonLayout() {
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        cancel.addThemeVariants(ButtonVariant.LUMO_CONTRAST);

        save.addClickShortcut(Key.ENTER);
        cancel.addClickShortcut(Key.ESCAPE);

        save.addClickListener(event -> validateAndSave());
        cancel.addClickListener(event -> fireEvent(new CostingFormEvent.CloseEvent(this)));

        ingredientsBinder.addStatusChangeListener(e -> save.setEnabled(ingredientsBinder.isValid()));
        return new HorizontalLayout(save, cancel);
    }

    private void validateAndSave() {
        try {
            ingredientsBinder.writeBean(ingredients);
            fireEvent(new CostingFormEvent.SaveEvent(this, ingredients));
        } catch (ValidationException e) {
            e.printStackTrace();
        }
    }


    public static abstract class CostingFormEvent extends ComponentEvent<IngredientsForm> {

        private final Ingredients ingredients;

        public CostingFormEvent(IngredientsForm source, Ingredients ingredients) {
            super(source, false);
            this.ingredients = ingredients;
        }

        public Ingredients getCosting(){
            return ingredients;
        }

        public static class SaveEvent extends CostingFormEvent {
            SaveEvent(IngredientsForm source, Ingredients ingredients) {
                super(source, ingredients);
                source.currencyPrefix.setText(source.selectCurrency.getValue());
                source.currencyPrefix1.setText(source.selectCurrency.getValue());
                if (source.isVisible()) {
                    ingredients.setTotalCost(ingredients.getProductQuantity());
                    Notification.show(source.productName.getValue() + " " +
                                            " successfully updated",
                                    5000, Notification.Position.TOP_CENTER)
                            .addThemeVariants(NotificationVariant.LUMO_SUCCESS);
                }
            }
        }

        public static class CloseEvent extends CostingFormEvent {
            CloseEvent(IngredientsForm source) {
                super(source, null);
            }
        }
    }

    public <T extends ComponentEvent<?>> Registration addListener(Class<T> eventType,
                                                                  ComponentEventListener<T> listener) {
        return getEventBus().addListener(eventType, listener);
    }

}

