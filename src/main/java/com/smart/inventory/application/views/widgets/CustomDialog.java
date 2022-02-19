package com.smart.inventory.application.views.widgets;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.dialog.DialogVariant;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

import javax.annotation.Nonnull;

public class CustomDialog extends VerticalLayout {

    Button confirmBtn = new Button("Delete");


    public CustomDialog(@Nonnull Dialog dialog, String title) {
        H3 header = new H3("Delete selected " + title + "?");

        dialog.addThemeVariants(DialogVariant.LUMO_NO_PADDING);

        var text = new Text("Are you sure you want to permanently delete this " + title + "?");

        var cancelBtn = new Button("Cancel");
        cancelBtn.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        confirmBtn.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_ERROR);

        cancelBtn.addClickListener(cancelEvent -> dialog.close());
        HorizontalLayout buttonLayout = new HorizontalLayout(cancelBtn, confirmBtn);
        buttonLayout.setWidthFull();
        buttonLayout.getStyle().set("flex-wrap", "wrap");
        buttonLayout.setJustifyContentMode(JustifyContentMode.END);
        add(header, text, buttonLayout);
    }

    public void confirm(ComponentEventListener<ClickEvent<?>> listener) {
        confirmBtn.addClickListener(listener::onComponentEvent);
    }
}
