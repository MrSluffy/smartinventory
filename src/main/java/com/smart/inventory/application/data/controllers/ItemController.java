package com.smart.inventory.application.data.controllers;

import com.smart.inventory.application.data.entity.Item;
import com.smart.inventory.application.data.repository.ItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/item")
public class ItemController {

    @Autowired
    private ItemRepository itemRepository;

    @GetMapping
    List<Item> getItem(){
        return itemRepository.findAll();
    }

    @PostMapping
    Item createItem(@RequestBody Item item){
        return itemRepository.save(item);
    }


}
