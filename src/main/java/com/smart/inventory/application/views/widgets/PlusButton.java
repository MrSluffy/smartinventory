package com.smart.inventory.application.views.widgets;

import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;

public class PlusButton extends Button{

    public PlusButton(){
        super(new Icon(VaadinIcon.PLUS));
        addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_ICON);
        setClassName("fab-plus");
        setMaxHeight(8f, Unit.EX);
        setWidth(8f, Unit.EX);
        getStyle().set("margin-inline-end", "auto");
        getElement().setAttribute("aria-label", "Add item");
        setAutofocus(true);
    }
}
