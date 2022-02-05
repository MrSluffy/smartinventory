package com.smart.inventory.application.data.controllers;

import com.smart.inventory.application.data.entity.Owner;
import com.smart.inventory.application.data.repository.OwnerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/owner")
public class OwnerController {

    @Autowired
    private OwnerRepository ownerRepository;

    @GetMapping
    List<Owner> getOwner(){
        return ownerRepository.findAll();
    }

    @PostMapping
    Owner createNewOwner(@RequestBody Owner owner){
        return ownerRepository.save(owner);
    }
}
