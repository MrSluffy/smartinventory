package com.smart.inventory.application.data.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.smart.inventory.application.data.AbstractEntity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.Set;

@Entity
public class TodoItem extends AbstractEntity {

    private String title = "";

    @NotNull
    @JsonIgnore
    @ManyToMany(mappedBy = "todoItems")
    private final Set<Company> companySet = new HashSet<>();

    @JsonIgnore
    @ManyToOne(cascade={CascadeType.MERGE,
            CascadeType.REFRESH}, fetch = FetchType.LAZY)
    @JoinColumn(name = "employer_id", referencedColumnName = "id")
    private Employer employer;

    @OneToMany(mappedBy = "todoItem", orphanRemoval = true)
    private Set<Employer> employerSet = new HashSet<>();

    @JsonIgnore
    @ManyToOne(cascade={CascadeType.MERGE,
            CascadeType.REFRESH}, fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id", referencedColumnName = "id")
    private Company company;

    public TodoItem(){
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public Employer getEmployer() {
        return employer;
    }

    public void setEmployer(Employer employer) {
        this.employer = employer;
    }

    public Set<Company> getCompanySet() {
        return companySet;
    }

    public Set<Employer> getEmployerSet() {
        return employerSet;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
