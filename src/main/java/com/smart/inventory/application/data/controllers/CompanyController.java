package com.smart.inventory.application.data.controllers;

import com.smart.inventory.application.data.entity.Company;
import com.smart.inventory.application.data.repository.CompanyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/company")
public class CompanyController {

    @Autowired
    private CompanyRepository companyRepository;

    @GetMapping
    List<Company> getCompany(){
        return companyRepository.findAll();
    }

    @PostMapping
    Company createCompany(@RequestBody Company company){
        return companyRepository.save(company);
    }
}
