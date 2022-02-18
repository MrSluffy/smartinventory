package com.smart.inventory.application.exeptions;

public class ValueExeptionHandler extends IllegalStateException{

    public ValueExeptionHandler(String message){
        super(message);
    }
}
