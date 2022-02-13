package com.smart.inventory.application.views.widgets;

import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;

public class DeleteButton extends Button {

    public DeleteButton(){
        super(new Icon(VaadinIcon.CLOSE_SMALL));
        addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_ICON, ButtonVariant.LUMO_ERROR);
        setClassName("fab-del");
        setMaxHeight(8f, Unit.EX);
        setWidth(8f, Unit.EX);
        getStyle().set("margin-inline-end", "auto");
        getElement().setAttribute("aria-label", "Add item");
        setAutofocus(true);
    }

}
