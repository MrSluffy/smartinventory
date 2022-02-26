package com.smart.inventory.application.data.services.solditem;

import com.smart.inventory.application.data.entity.Item;
import com.smart.inventory.application.data.entity.SoldItem;
import com.smart.inventory.application.data.repository.IItemRepository;
import com.smart.inventory.application.data.repository.ISoldItemRepository;
import com.smart.inventory.application.exeptions.NotFoundExeption;
import com.smart.inventory.application.util.Utilities;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Nonnull;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class SoldItemService implements ISoldItemService {

    private final ISoldItemRepository soldItemRepository;
    private final IItemRepository itemRepository;

    @Autowired
    public SoldItemService(ISoldItemRepository soldItemRepository,
                           IItemRepository itemRepository) {
        this.soldItemRepository = soldItemRepository;
        this.itemRepository = itemRepository;
    }


    @Override
    public void deleteSelectedSoldItem(List<SoldItem> soldItems) {
        soldItemRepository.deleteAll(soldItems);
    }

    @Override
    public void addNewSoldItem(Item items, String description, Integer quantity, @Nonnull Utilities utilities) {
        SoldItem soldItem = new SoldItem();
        soldItem.setItem(items);
        if(utilities.employer != null) {
            soldItem.getAddedByEmployer().add(utilities.employer);
        }

        if(utilities.owner != null){
            soldItem.getAddedByOwner().add(utilities.owner);
        }
        utils(items, description, quantity, utilities, soldItem);
    }

    @Override
    public List<Item> findAllItems(Integer companyId) {
        return itemRepository.search(companyId);
    }

    @Override
    public List<SoldItem> findAllSoldItems(Integer id) {
        return soldItemRepository.search(id);
    }

    public SoldItem getSoldItemById(Integer id){
        return soldItemRepository.findById(id).orElseThrow(NotFoundExeption::new);
    }


    @Transactional
    @Override
    public void updateSoldItem(Integer id, String description, Item item, Integer quantity, Utilities utilities) {

        SoldItem soldItem = getSoldItemById(id);
        utils(item, description, quantity, utilities, soldItem);
    }

    public void utils(Item item, String description, Integer quantity, Utilities utilities, SoldItem soldItem) {
        soldItem.setItemName(item.getItemName());
        soldItem.setDescription(description);
        soldItem.setQuantity(quantity);
        soldItem.setPurchaseAmount(item.getPrice() * quantity);
        soldItem.getCompany().add(utilities.company);
        soldItem.setSoldItemCompany(utilities.company);
        soldItem.setDateAndTime(LocalDateTime.now().toLocalTime().toString().substring(0, 5)
                + "-" + LocalDateTime.now().toLocalDate().toString());
        soldItemRepository.save(soldItem);
    }
}
