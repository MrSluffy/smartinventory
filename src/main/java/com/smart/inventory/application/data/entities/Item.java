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

    @NotNull
    @JsonIgnore
    @ManyToMany(mappedBy = "itemInCompany")
    private final Set<Company> company = new HashSet<>();

    @OneToMany(mappedBy = "item", orphanRemoval = true)
    private Set<Employer> addedByEmployer = new HashSet<>();

    @OneToMany(mappedBy = "itemOwner", orphanRemoval = true)
    private Set<Owner> addedByOwner = new HashSet<>();

    @JsonIgnore
    @ManyToOne(cascade={CascadeType.MERGE,
            CascadeType.REFRESH}, fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id", referencedColumnName = "id")
    private Company itemCompany;

    @JsonIgnore
    @ManyToOne(cascade={CascadeType.MERGE,
            CascadeType.REFRESH}, fetch = FetchType.LAZY)
    @JoinColumn(name = "activity_id", referencedColumnName = "id")
    private Activity activity;

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

    public String getDateAndTime() {
        return dateAndTime;
    }

    public void setDateAndTime(String dateAndTime) {
        this.dateAndTime = dateAndTime;
    }

    public Set<Owner> getAddedByOwner() {
        return addedByOwner;
    }


    public Set<Employer> getAddedByEmployer() {
        return addedByEmployer;
    }


    public Company getItemCompany() {
        return itemCompany;
    }

    public void setItemCompany(Company itemCompany) {
        this.itemCompany = itemCompany;
    }

    public Set<Company> getCompany() {
        return company;
    }


    public Activity getActivity() {
        return activity;
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
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
