package com.smart.inventory.application.data.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.smart.inventory.application.data.AbstractEntity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Activity extends AbstractEntity {


    private String activityTitle;

    private String activitySubTitle;

    private String activityLocation;

    private String date;

    @JsonIgnore
    @ManyToOne(cascade={CascadeType.MERGE,
            CascadeType.REFRESH}, fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id", referencedColumnName = "id")
    private Company company;

    @NotNull
    @JsonIgnore
    @ManyToMany(mappedBy = "activityInCompany")
    private final Set<Company> companySet = new HashSet<>();

    @OneToMany(mappedBy = "activity", orphanRemoval = true)
    private Set<Employer> employer = new HashSet<>();

    @OneToMany(mappedBy = "activity", orphanRemoval = true)
    private Set<Owner> owner = new HashSet<>();

    @OneToMany(mappedBy = "activity", orphanRemoval = true)
    private Set<Item> item = new HashSet<>();


    public Set<Employer> getEmployer() {
        return employer;
    }

    public Set<Owner> getOwner() {
        return owner;
    }

    public Set<Company> getCompanySet() {
        return companySet;
    }

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public String getActivityTitle() {
        return activityTitle;
    }

    public void setActivityTitle(String activityTitle) {
        this.activityTitle = activityTitle;
    }

    public String getActivityLocation() {
        return activityLocation;
    }

    public void setActivityLocation(String activityLocation) {
        this.activityLocation = activityLocation;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getActivitySubTitle() {
        return activitySubTitle;
    }

    public void setActivitySubTitle(String activitySubTitle) {
        this.activitySubTitle = activitySubTitle;
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
