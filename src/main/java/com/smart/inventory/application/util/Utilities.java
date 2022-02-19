package com.smart.inventory.application.util;

import com.smart.inventory.application.data.entity.Company;
import com.smart.inventory.application.data.entity.Employer;
import com.smart.inventory.application.data.entity.Owner;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextFieldVariant;
import com.vaadin.flow.server.VaadinSession;

import javax.annotation.Nonnull;

public class Utilities {

    public final Employer employer = VaadinSession.getCurrent().getAttribute(Employer.class);
    public final Owner owner = VaadinSession.getCurrent().getAttribute(Owner.class);
    public final Company company = VaadinSession.getCurrent().getAttribute(Company.class);

    public Utilities(){
    }

    @Nonnull
    public static Boolean caseInsensitiveContainsFilter(@Nonnull String value, @Nonnull String filter) {
        return value.toLowerCase().contains(filter.toLowerCase());
    }

    @Nonnull
    public static Component getComponent(@Nonnull Select<String> selectCurrency,
                                         @Nonnull Div currencyPrefix,
                                         @Nonnull Div currencyPrefix1,
                                         @Nonnull NumberField price,
                                         @Nonnull NumberField totalPrice) {
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
