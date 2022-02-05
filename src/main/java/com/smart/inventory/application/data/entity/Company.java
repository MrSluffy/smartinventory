package com.smart.inventory.application.data.entity;

import com.smart.inventory.application.data.AbstractEntity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Company extends AbstractEntity {


    @ManyToMany
    @JoinTable(
            name = "company_employers",
            joinColumns = @JoinColumn(name = "company_id"),
            inverseJoinColumns = @JoinColumn(name = "employer_id")
    )
    List<Employer> employersInCompany = new ArrayList<>();


    private String name;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "owner_id", referencedColumnName = "id")
    private Owner owner;


    public Company(){
    }

    public Owner getOwner() {
        return owner;
    }

    public void setOwner(Owner owner) {
        this.owner = owner;
    }

    public List<Employer> getEmployersInCompany() {
        return employersInCompany;
    }

    public void setEmployersInCompany(List<Employer> employers) {
        this.employersInCompany = employers;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
