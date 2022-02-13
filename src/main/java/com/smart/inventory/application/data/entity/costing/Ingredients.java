package com.smart.inventory.application.data.entity.costing;

import com.smart.inventory.application.data.AbstractEntity;

import javax.persistence.Entity;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Entity
public class Ingredients extends AbstractEntity {

    @NotEmpty
    private String productName = "";

    @NotNull
    private double productPrice;

    private double totalCost;

    @NotNull
    private int productQuantity;


    public Ingredients(){
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public double getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(double productPrice) {
        this.productPrice = productPrice;
    }

    public double getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(double totalCost) {
        this.totalCost = totalCost * productPrice;
    }

    public int getProductQuantity() {
        return productQuantity;
    }

    public void setProductQuantity(int productQuantity) {
        this.productQuantity = productQuantity;
    }

    @Override
    public String toString() {
        return "Ingredients{" +
                "productName='" + productName + '\'' +
                ", productPrice=" + productPrice +
                ", totalCost=" + totalCost +
                ", productQuantity=" + productQuantity +
                '}';
    }
}
