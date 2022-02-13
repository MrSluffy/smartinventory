package com.smart.inventory.application.data.entity;

import com.smart.inventory.application.data.AbstractEntity;
import org.joda.time.DateTime;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Item extends AbstractEntity {

    @NotEmpty
    private String itemName = "";

    @NotNull
    private int quantity;

    @NotNull
    private double price;

    private double totalPrice;

    @NotNull
    private String dateAndTime;

    @OneToMany(mappedBy = "item")
    private Set<Employer> addedBy = new HashSet<>();

    public Item(){
        dateAndTime = LocalDateTime.now().toString();
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String dt(){
        return DateTime.now().toString();
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double value) {
        this.totalPrice = value * this.price;
    }

    public Set<Employer> getAddedBy() {
        return addedBy;
    }

    public void setAddedBy(Set<Employer> addedBy) {
        this.addedBy = addedBy;
    }

    public String getDateAndTime() {
        return dateAndTime;
    }

    public void setDateAndTime(String dateAndTime) {
        this.dateAndTime = dateAndTime;
    }
}
