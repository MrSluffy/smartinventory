package com.smart.inventory.application.data.entities.ingredients;

import com.smart.inventory.application.data.AbstractEntity;

import javax.persistence.Entity;
import java.util.Objects;

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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        QuantityUnit that = (QuantityUnit) o;
        return Objects.equals(unitName, that.unitName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), unitName);
    }
}
