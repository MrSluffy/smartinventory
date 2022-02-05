package com.smart.inventory.application.data.controllers;

import com.smart.inventory.application.data.entity.Buyer;
import com.smart.inventory.application.data.repository.BuyerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/buyer")
public class BuyerController {

    @Autowired
    private BuyerRepository buyerRepository;

    @GetMapping
    List<Buyer> getBuyer(){
        return buyerRepository.findAll();
    }

    @PostMapping
    Buyer addNewBuyer(@RequestBody Buyer buyer){
        return buyerRepository.save(buyer);
    }
}
