package com.smart.inventory.application.data.controllers;

import com.smart.inventory.application.data.entity.Employer;
import com.smart.inventory.application.data.repository.EmployerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/employer")
public class EmployerController {

    @Autowired
    private EmployerRepository employerRepository;

    @GetMapping
    List<Employer> getEmployer(){
        return employerRepository.findAll();
    }

    @PostMapping
    Employer addNewEmployer(@RequestBody Employer employer){
        return employerRepository.save(employer);
    }

}
