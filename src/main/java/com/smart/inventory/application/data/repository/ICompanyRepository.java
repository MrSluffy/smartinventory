package com.smart.inventory.application.data.repository;

import com.smart.inventory.application.data.entity.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ICompanyRepository extends JpaRepository<Company, Integer> {

    Company getCompanyByName(String companyName);
}
