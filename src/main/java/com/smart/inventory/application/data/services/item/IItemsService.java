package com.smart.inventory.application.data.services.item;

import com.smart.inventory.application.data.entities.Item;
import com.smart.inventory.application.util.Utilities;

import java.util.List;

public interface IItemsService {

    List<Item> findAllItem();

    List<Item> findAllItem(Integer items);

    long countItems();

    void deleteItemSelected(List<Item> item);

    void addNewItem(Item item, Utilities utilities);

    void updateItem(Integer id, String name, int quantity, Double price, Double totalPrice, Utilities utilities);
}
