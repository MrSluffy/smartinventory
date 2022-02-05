package com.smart.inventory.application.data.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.smart.inventory.application.data.AbstractEntity;

import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.validation.constraints.NotEmpty;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Buyer extends AbstractEntity {

    @NotEmpty
    private String name = "";

    @JsonIgnore
    @ManyToMany(mappedBy = "itemName")
    private Set<Item> item = new HashSet<>();

    public Buyer(){
    }

    private double price;

    private int piece;

    private double totalPrice;

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

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getPiece() {
        return piece;
    }

    public void setPiece(int piece) {
        this.piece = piece;
    }

    public double getTotalPrice() {
        return totalPrice ;
    }

    public void setTotalPrice(double value) {
        this.totalPrice = value * price;
    }
}
