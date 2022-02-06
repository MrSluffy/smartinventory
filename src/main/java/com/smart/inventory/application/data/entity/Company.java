package com.smart.inventory.application.data.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.smart.inventory.application.data.AbstractEntity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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


    private String name;

    @JsonIgnore
    @ManyToOne(cascade={CascadeType.ALL, CascadeType.DETACH})
    @JoinColumn(name = "owner_id", referencedColumnName = "id")
    private Owner owner;

    @JsonIgnore
    @OneToMany(mappedBy = "emplyrCompany", orphanRemoval = true)
    private Set<Employer> emplyr = new HashSet<>();


    public Company(){
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
}