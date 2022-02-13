package com.smart.inventory.application.views.widgets;

import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;

public class FilterText extends TextField {

    public FilterText(){
        setWidth(20f, Unit.EM);
        setClearButtonVisible(true);
        setValueChangeMode(ValueChangeMode.LAZY);
    }
}
