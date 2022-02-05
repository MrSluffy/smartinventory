package com.smart.inventory.application.data.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.smart.inventory.application.data.AbstractEntity;
import org.joda.time.DateTime;

import javax.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Item extends AbstractEntity {

    private String itemName;

    private int piece;

    private double price;

    private double totalPrice;

    private String strDate;

    @OneToMany(mappedBy = "item")
    private Set<Employer> addedBy = new HashSet<>();

    @Basic
    @Temporal(TemporalType.TIMESTAMP)
    private Date sqlTimestamp;

    @JsonIgnore
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "buyer_id", referencedColumnName = "id")
    private Buyer buyer;

    public Item(){
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
        return strDate;
    }

    public String dt(){
        return DateTime.now().toString();
    }

    public void setStrDate(String strDate) {
        this.strDate = strDate;
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

    public Buyer getBuyer() {
        return buyer;
    }

    public void setBuyer(Buyer buyer) {
        this.buyer = buyer;
    }
}
