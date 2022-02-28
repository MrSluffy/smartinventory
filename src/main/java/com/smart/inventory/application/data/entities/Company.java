package com.smart.inventory.application.data.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.smart.inventory.application.data.AbstractEntity;
import com.smart.inventory.application.data.entities.ingredients.Ingredients;

import javax.persistence.*;
import java.util.*;

@Entity
public class Company extends AbstractEntity {


    @JsonIgnore
    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name = "company_owners",
            joinColumns = @JoinColumn(name = "companys_id"),
            inverseJoinColumns = @JoinColumn(name = "owners_id")
    )
    List<Owner> ownerInCompany = new ArrayList<>();


    @JsonIgnore
    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name = "company_items",
            joinColumns = @JoinColumn(name = "companys_id"),
            inverseJoinColumns = @JoinColumn(name = "item_id")
    )
    List<Item> itemInCompany = new ArrayList<>();

    @JsonIgnore
    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name = "company_solditems",
            joinColumns = @JoinColumn(name = "companys_id"),
            inverseJoinColumns = @JoinColumn(name = "item_id")
    )
    List<SoldItem> soldItemInCompany = new ArrayList<>();



    @JsonIgnore
    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name = "company_ingredient",
            joinColumns = @JoinColumn(name = "companys_id"),
            inverseJoinColumns = @JoinColumn(name = "ingredient_id")
    )
    List<Ingredients> ingredientsInCompany = new ArrayList<>();


    private String name;

    @JsonIgnore
    @ManyToOne(cascade={CascadeType.ALL, CascadeType.DETACH})
    @JoinColumn(name = "owner_id", referencedColumnName = "id")
    private Owner owner;

    @JsonIgnore
    @OneToMany(mappedBy = "emplyrCompany", orphanRemoval = true)
    private Set<Employer> emplyr = new HashSet<>();

    @JsonIgnore
    @OneToMany(mappedBy = "itemCompany", orphanRemoval = true)
    private Set<Item> companyItems = new HashSet<>();

    @JsonIgnore
    @OneToMany(mappedBy = "ingredientCompany", orphanRemoval = true)
    private Set<Ingredients> companyIngredients = new HashSet<>();


    public Company(){
    }

    public List<Item> getItemInCompany() {
        return itemInCompany;
    }

    public Owner getOwner() {
        return owner;
    }

    public void setOwner(Owner owner) {
        this.owner = owner;
    }

    public List<Owner> getOwnerInCompany() {
        return ownerInCompany;
    }

    public void setOwnerInCompany(List<Owner> owners) {
        this.ownerInCompany = owners;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<Employer> getEmplyr() {
        return emplyr;
    }

    public Set<Item> getCompanyItems() {
        return companyItems;
    }

    public void setCompanyItems(Set<Item> companyItems) {
        this.companyItems = companyItems;
    }

    public Set<Ingredients> getCompanyIngredients() {
        return companyIngredients;
    }

    public void setCompanyIngredients(Set<Ingredients> companyIngredients) {
        this.companyIngredients = companyIngredients;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Company company = (Company) o;
        return Objects.equals(ownerInCompany, company.ownerInCompany) && Objects.equals(itemInCompany, company.itemInCompany) && Objects.equals(soldItemInCompany, company.soldItemInCompany) && Objects.equals(ingredientsInCompany, company.ingredientsInCompany) && Objects.equals(name, company.name) && Objects.equals(owner, company.owner) && Objects.equals(emplyr, company.emplyr) && Objects.equals(companyItems, company.companyItems) && Objects.equals(companyIngredients, company.companyIngredients);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), ownerInCompany, itemInCompany, soldItemInCompany, ingredientsInCompany, name, owner, emplyr, companyItems, companyIngredients);
    }
}
