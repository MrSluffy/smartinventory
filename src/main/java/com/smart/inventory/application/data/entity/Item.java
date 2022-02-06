package com.smart.inventory.application.data.entity;

import com.smart.inventory.application.data.AbstractEntity;
import org.joda.time.DateTime;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Item extends AbstractEntity {

    @NotEmpty
    private String itemName = "";

    private int piece;

    private double price;

    private double totalPrice;

    private String dateAndTime;

    @OneToMany(mappedBy = "item")
    private Set<Employer> addedBy = new HashSet<>();

    @Basic
    @Temporal(TemporalType.TIMESTAMP)
    private Date sqlTimestamp;

    public Item(){
        dateAndTime = LocalDateTime.now().toString();
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public int getPiece() {
        return piece;
    }

    public void setPiece(int piece) {
        this.piece = piece;
    }

    public String getStrDate() {
        return dateAndTime;
    }

    public String dt(){
        return DateTime.now().toString();
    }

    public void setDate(String date) {
        this.dateAndTime = date;
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

}
