package com.smart.inventory.application.data.entity;

import com.smart.inventory.application.data.AbstractEntity;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Customer extends AbstractEntity {

    @NotEmpty
    private String name = "";

    @ManyToMany(mappedBy = "itemName")
    private Set<Item> item = new HashSet<>();

    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "item_id", referencedColumnName = "id")
    private Item soldItem;

    @ManyToMany(mappedBy = "email")
    private Set<Employer> addedBy = new HashSet<>();

    public Customer(){
    }

    private double purchaseAmount;

    private int quantity;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<Item> getItem() {
        return item;
    }

    public void setItem(Set<Item> item) {
        this.item = item;
    }

    public double getPurchaseAmount() {
        return purchaseAmount;
    }

    public void setPurchaseAmount(double purchaseAmount) {
        this.purchaseAmount = purchaseAmount;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public Set<Employer> getAddedBy() {
        return addedBy;
    }

    public void setAddedBy(Set<Employer> addedBy) {
        this.addedBy = addedBy;
    }

    public Item getSoldItem() {
        return soldItem;
    }

    public void setSoldItem(Item soldItem) {
        this.soldItem = soldItem;
    }
}
