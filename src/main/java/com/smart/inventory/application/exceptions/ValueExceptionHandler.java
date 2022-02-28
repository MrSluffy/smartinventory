package com.smart.inventory.application.exceptions;

public class ValueExceptionHandler extends IllegalStateException{

    public ValueExceptionHandler(String message){
        super(message);
    }
}
