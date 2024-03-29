package com.smart.inventory.application.data.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.smart.inventory.application.data.AbstractEntity;
import com.smart.inventory.application.data.Role;
import com.smart.inventory.application.data.entities.ingredients.Ingredients;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.RandomStringUtils;

import javax.annotation.Nonnull;
import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import java.util.Objects;
import java.util.Set;

@Entity
public class Owner extends AbstractEntity {

    @NotEmpty
    private String firstName = "";

    @NotEmpty
    private String lastName = "";

    @Email
    @NotEmpty
    private String email = "";

    @JsonIgnore
    @OneToMany(mappedBy = "owner")
    private Set<Company> company;

    private String passwordSalt;
    private String passwordHash;

    private Role roles;

    private String signMethod;

    @JsonIgnore
    @ManyToOne(cascade={CascadeType.MERGE,
            CascadeType.REFRESH}, fetch = FetchType.LAZY)
    private Item itemOwner;

    @JsonIgnore
    @ManyToOne(cascade={CascadeType.MERGE,
            CascadeType.REFRESH}, fetch = FetchType.LAZY)
    private SoldItem soldItemOwner;


    @JsonIgnore
    @ManyToOne(cascade={CascadeType.MERGE,
            CascadeType.REFRESH}, fetch = FetchType.LAZY)
    private Ingredients ingredientsOwner;


    public Owner(){}

    public Owner(String firstName, String lastName, String email,
                 String password, Role roles){
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.roles = roles;
        this.passwordSalt = RandomStringUtils.random(32);
        this.passwordHash = DigestUtils.sha1Hex(password + passwordSalt);
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

    public String getPasswordSalt() {
        return passwordSalt;
    }

    public void setPasswordSalt(String passwordSalt) {
        this.passwordSalt = passwordSalt;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    @Nonnull
    public Role getRoles() {
        return roles;
    }

    public void setRoles(@Nonnull Role roles) {
        this.roles = roles;
    }

    public String getSignMethod() {
        return signMethod;
    }

    public void setSignMethod(String signMethod) {
        this.signMethod = signMethod;
    }

    public Set<Company> getCompany() {
        return company;
    }

    public void setCompany(Set<Company> company) {
        this.company = company;
    }

    public Item getItemOwner() {
        return itemOwner;
    }

    public void setItemOwner(Item itemOwner) {
        this.itemOwner = itemOwner;
    }

    public Ingredients getIngredientsOwner() {
        return ingredientsOwner;
    }

    public void setIngredientsOwner(Ingredients ingredientsOwner) {
        this.ingredientsOwner = ingredientsOwner;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Owner owner = (Owner) o;
        return Objects.equals(firstName, owner.firstName) && Objects.equals(lastName, owner.lastName) && Objects.equals(email, owner.email) && Objects.equals(company, owner.company) && Objects.equals(passwordSalt, owner.passwordSalt) && Objects.equals(passwordHash, owner.passwordHash) && roles == owner.roles && Objects.equals(signMethod, owner.signMethod) && Objects.equals(itemOwner, owner.itemOwner) && Objects.equals(soldItemOwner, owner.soldItemOwner) && Objects.equals(ingredientsOwner, owner.ingredientsOwner);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), firstName, lastName, email, company, passwordSalt, passwordHash, roles, signMethod, itemOwner, soldItemOwner, ingredientsOwner);
    }
}
