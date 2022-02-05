package com.smart.inventory.application.data.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.smart.inventory.application.data.AbstractEntity;
import com.smart.inventory.application.data.Role;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.RandomStringUtils;

import javax.annotation.Nonnull;
import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
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

    @ElementCollection(fetch = FetchType.EAGER)
    @Nonnull
    private Set<Role> roles;

    private String signMethod;


    public Owner(){}

    public Owner(@NotNull String signMethod, String email, String password, @Nonnull Set<Role> roles){
        this.signMethod = signMethod;
        this.email = email;
        this.roles = roles;
        this.passwordSalt = RandomStringUtils.random(32);
        this.passwordHash =  DigestUtils.sha1Hex(password + passwordSalt);
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
    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(@Nonnull Set<Role> roles) {
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
}
