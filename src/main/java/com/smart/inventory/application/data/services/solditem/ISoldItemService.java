package com.smart.inventory.application.data.services.solditem;

import com.smart.inventory.application.data.entities.SoldItem;
import com.smart.inventory.application.data.entities.Item;
import com.smart.inventory.application.util.Utilities;

import java.util.List;

public interface ISoldItemService {

    void deleteSelectedSoldItem(List<SoldItem> soldItems);

    void addNewSoldItem(Item items, String description, int quantity,Utilities utilities);

    List<Item> findAllItems(Integer companyId);

    List<SoldItem> findAllSoldItems(Integer id);

    void updateSoldItem(Integer id, String description, Item item, int quantity, Utilities utilities);
}
