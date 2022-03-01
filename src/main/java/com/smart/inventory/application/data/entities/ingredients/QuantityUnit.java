package com.smart.inventory.application.data.entities.ingredients;

import com.smart.inventory.application.data.AbstractEntity;

import javax.persistence.Entity;

@Entity
public class QuantityUnit extends AbstractEntity {

    public String unitName;

    public QuantityUnit(){
    }

    public QuantityUnit(String unitName){
        this.unitName = unitName;
    }

    public String getUnitName() {
        return unitName;
    }

    public void setUnitName(String unitName) {
        this.unitName = unitName;
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }
}
