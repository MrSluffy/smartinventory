package com.smart.inventory.application.views.extension;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextFieldVariant;

public class CusComponent {

    public static Component getComponent(Select<String> selectCurrency, Div currencyPrefix, Div currencyPrefix1, NumberField price, NumberField totalPrice) {
        selectCurrency.setWidth(10f, Unit.EX);
        selectCurrency.setLabel("Currency");
        selectCurrency.setItems("₱", "$");
        selectCurrency.setValue("₱");

        currencyPrefix.setText(selectCurrency.getValue());
        currencyPrefix1.setText(selectCurrency.getValue());
        selectCurrency.addValueChangeListener(selectStringComponentValueChangeEvent -> {
            currencyPrefix.setText(selectCurrency.getValue());
            currencyPrefix1.setText(selectCurrency.getValue());
        });
        price.setPrefixComponent(currencyPrefix);
        price.addThemeVariants(TextFieldVariant.LUMO_ALIGN_RIGHT);
        price.setSizeFull();
        totalPrice.setPrefixComponent(currencyPrefix1);

        return new HorizontalLayout(selectCurrency, price);
    }
}
