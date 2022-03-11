package com.smart.inventory.application.views.widgets;

import com.vaadin.flow.component.html.Div;

public class MaterialCardBackground extends Div {





    public MaterialCardBackground(){
        addClassName("material-background");
        addClassNames("border-b", "border-contrast-40");
        setSizeFull();

    }
}
