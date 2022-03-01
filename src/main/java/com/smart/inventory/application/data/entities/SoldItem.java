package com.smart.inventory.application.data.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.smart.inventory.application.data.AbstractEntity;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
public class SoldItem extends AbstractEntity {

    @NotEmpty
    private String description = "";

    @ManyToOne
    private Item item;

    @OneToMany(mappedBy = "soldItem", orphanRemoval = true)
    private Set<Employer> addedByEmployer = new HashSet<>();

    @OneToMany(mappedBy = "soldItemOwner", orphanRemoval = true)
    private Set<Owner> addedByOwner = new HashSet<>();

    @NotNull
    @JsonIgnore
    @ManyToMany(mappedBy = "soldItemInCompany")
    private final Set<Company> company = new HashSet<>();

    @JsonIgnore
    @ManyToOne(cascade={CascadeType.MERGE,
            CascadeType.REFRESH}, fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id", referencedColumnName = "id")
    private Company soldItemCompany;


    private String dateAndTime;

    private String itemName;

    public SoldItem(){
        dateAndTime = LocalDateTime.now().toString();
    }

    private double purchaseAmount;

    private int quantity;


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

    public String getDateAndTime() {
        return dateAndTime;
    }

    public void setDateAndTime(String dateAndTime) {
        this.dateAndTime = dateAndTime;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Set<Owner> getAddedByOwner() {
        return addedByOwner;
    }

    public void setAddedByOwner(Set<Owner> addedByOwner) {
        this.addedByOwner = addedByOwner;
    }

    public Set<Employer> getAddedByEmployer() {
        return addedByEmployer;
    }

    public void setAddedByEmployer(Set<Employer> addedByEmployer) {
        this.addedByEmployer = addedByEmployer;
    }

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    public Set<Company> getCompany() {
        return company;
    }

    public Company getSoldItemCompany() {
        return soldItemCompany;
    }

    public void setSoldItemCompany(Company soldItemCompany) {
        this.soldItemCompany = soldItemCompany;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }
}
