package com.smart.inventory.application.data.entities.ingredients;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.smart.inventory.application.data.AbstractEntity;
import com.smart.inventory.application.data.entities.Company;
import com.smart.inventory.application.data.entities.Employer;
import com.smart.inventory.application.data.entities.Owner;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
public class Ingredients extends AbstractEntity {

    @NotEmpty
    private String productName = "";

    @NotNull
    private double productPrice;

    private double totalCost;

    @NotNull
    private int productQuantity;

    @NotNull
    @JsonIgnore
    @ManyToMany(mappedBy = "ingredientsInCompany")
    private final Set<Company> company = new HashSet<>();

    @OneToMany(mappedBy = "ingredients", orphanRemoval = true)
    private Set<Employer> addedByEmployer = new HashSet<>();

    @OneToMany(mappedBy = "ingredientsOwner", orphanRemoval = true)
    private Set<Owner> addedByOwner = new HashSet<>();

    @JsonIgnore
    @ManyToOne(cascade={CascadeType.MERGE,
            CascadeType.REFRESH}, fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id", referencedColumnName = "id")
    private Company ingredientCompany;

    @NotNull
    @ManyToOne
    private QuantityUnit quantityUnit;


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

    public void setTotalCost(double value) {
        this.totalCost = value * productPrice;
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

    public QuantityUnit getQuantityUnit() {
        return quantityUnit;
    }

    public void setQuantityUnit(QuantityUnit quantityUnit) {
        this.quantityUnit = quantityUnit;
    }

    public Company getIngredientCompany() {
        return ingredientCompany;
    }

    public void setIngredientCompany(Company ingredientCompany) {
        this.ingredientCompany = ingredientCompany;
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

    public Set<Company> getCompany() {
        return company;
    }


}
