package com.smart.inventory.application.data.controllers;

import com.smart.inventory.application.data.entity.Position;
import com.smart.inventory.application.data.service.SmartInventoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/position")
public class PositionController {

    @Autowired
    private SmartInventoryService smartInventoryService;



    @DeleteMapping("/{id}")
    private void delete(Position position){
        smartInventoryService.deletePosition(position);
    }
}
