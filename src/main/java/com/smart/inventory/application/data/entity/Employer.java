package com.smart.inventory.application.data.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.smart.inventory.application.data.AbstractEntity;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Employer extends AbstractEntity {

    @NotEmpty
    private String firstName = "";

    @NotEmpty
    private String lastName = "";

    @Email
    @NotEmpty
    private String email = "";

    @JsonIgnore
    @ManyToMany(mappedBy = "ownerInCompany")
    private Set<Company> company = new HashSet<>();

    @NotNull
    @ManyToOne
    private Position position;

    @JsonIgnore
    @ManyToOne(cascade={CascadeType.MERGE,
            CascadeType.REFRESH}, fetch = FetchType.LAZY)
    @NotFound(
            action = NotFoundAction.IGNORE)
    @JoinColumn(name = "buyer_id", referencedColumnName = "id")
    private Buyer buyer;

    @JsonIgnore
    @ManyToOne(cascade={CascadeType.ALL, CascadeType.DETACH})
    @JoinColumn(name = "company_id", referencedColumnName = "id")
    private Company emplyrCompany;

    @JsonIgnore
    @ManyToOne(cascade={CascadeType.MERGE, CascadeType.DETACH})
    @JoinColumn(name = "item_id", referencedColumnName = "id")
    private Item item;


    private String passwordSalt;
    private String passwordHash;

    private String signMethod;

    public Employer(String signMethod, String email, String password){
        this.signMethod = signMethod;
        this.email = email;
        this.passwordSalt = RandomStringUtils.random(32);
        this.passwordHash = DigestUtils.sha1Hex(password + passwordSalt);
    }

    public Employer() {

    }

    public boolean checkPassword(String password){
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

    public String getSignMethod() {
        return signMethod;
    }

    public void setSignMethod(String signMethod) {
        this.signMethod = signMethod;
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

    public Buyer getBuyer() {
        return buyer;
    }

    public void setBuyer(Buyer buyer) {
        this.buyer = buyer;
    }
}
