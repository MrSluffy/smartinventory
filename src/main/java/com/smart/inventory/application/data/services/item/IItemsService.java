package com.smart.inventory.application.data.services.item;

import com.smart.inventory.application.data.entity.Item;
import com.smart.inventory.application.util.Utilities;

import java.util.List;

public interface IItemsService {

    List<Item> findAllItem();

    List<Item> findAllItem(String filterText);

    void saveItem(Item item, Utilities utilities);

    long countItems();

    void deleteItemSelected(List<Item> item);
}
