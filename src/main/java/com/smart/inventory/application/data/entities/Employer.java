package com.smart.inventory.application.data.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.smart.inventory.application.data.AbstractEntity;
import com.smart.inventory.application.data.Role;
import com.smart.inventory.application.data.entities.ingredients.Ingredients;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
public class Employer extends AbstractEntity {

    @NotEmpty
    @NotNull
    private String firstName = "";

    @NotEmpty
    @NotNull
    private String lastName = "";

    @Email
    @NotEmpty
    @NotNull
    private String email = "";

    @NotNull
    @JsonIgnore
    @ManyToMany(mappedBy = "ownerInCompany")
    private final Set<Company> company = new HashSet<>();

    @ManyToOne
    private Position position;

    @JsonIgnore
    @ManyToOne(cascade={CascadeType.MERGE,
            CascadeType.REFRESH}, fetch = FetchType.LAZY)
    @NotFound(
            action = NotFoundAction.IGNORE)
    @JoinColumn(name = "soldItem_id", referencedColumnName = "id")
    private SoldItem soldItem;

    @JsonIgnore
    @ManyToOne(cascade={CascadeType.MERGE,
            CascadeType.REFRESH}, fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id", referencedColumnName = "id")
    private Company emplyrCompany;

    @JsonIgnore
    @ManyToOne(cascade={CascadeType.MERGE,
            CascadeType.REFRESH}, fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id", referencedColumnName = "id")
    private Item item;

    @JsonIgnore
    @ManyToOne(cascade={CascadeType.MERGE,
            CascadeType.REFRESH}, fetch = FetchType.LAZY)
    @JoinColumn(name = "ingredient_id", referencedColumnName = "id")
    private Ingredients ingredients;

    private String passwordSalt;
    private String passwordHash;

    private Role roles;


    public Employer(String email, String firstName, String lastName, String password, Role role){
        this.firstName = firstName;
        this.lastName = lastName;
        this.roles = role;
        this.email = email;
        this.passwordSalt = RandomStringUtils.random(32);
        this.passwordHash = DigestUtils.sha1Hex(password + passwordSalt);
    }

    public Employer() {

    }

    public boolean verifyPassword(String password){
        return DigestUtils.sha1Hex(password + passwordSalt).equals(passwordHash);
    }

    @Override
    public String toString() {
        return firstName + " " + lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Set<Company> getCompany() {
        return company;
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    public Company getEmplyrCompany() {
        return emplyrCompany;
    }

    public void setEmplyrCompany(Company emplyrCompany) {
        this.emplyrCompany = emplyrCompany;
    }

    public Role getRoles() {
        return roles;
    }

    public void setRoles(Role roles) {
        this.roles = roles;
    }

    public Ingredients getIngredients() {
        return ingredients;
    }

    public void setIngredients(Ingredients ingredients) {
        this.ingredients = ingredients;
    }

    public SoldItem getSoldItem() {
        return soldItem;
    }

    public void setSoldItem(SoldItem soldItem) {
        this.soldItem = soldItem;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Employer employer = (Employer) o;
        return Objects.equals(firstName, employer.firstName) && Objects.equals(lastName, employer.lastName) && Objects.equals(email, employer.email) && Objects.equals(company, employer.company) && Objects.equals(position, employer.position) && Objects.equals(soldItem, employer.soldItem) && Objects.equals(emplyrCompany, employer.emplyrCompany) && Objects.equals(item, employer.item) && Objects.equals(ingredients, employer.ingredients) && Objects.equals(passwordSalt, employer.passwordSalt) && Objects.equals(passwordHash, employer.passwordHash) && roles == employer.roles;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), firstName, lastName, email, company, position, soldItem, emplyrCompany, item, ingredients, passwordSalt, passwordHash, roles);
    }
}
