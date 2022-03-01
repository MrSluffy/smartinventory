package com.smart.inventory.application.data.services.solditem;

import com.smart.inventory.application.data.entities.Item;
import com.smart.inventory.application.data.entities.SoldItem;
import com.smart.inventory.application.data.repository.IActivityRepository;
import com.smart.inventory.application.data.repository.IItemRepository;
import com.smart.inventory.application.data.repository.ISoldItemRepository;
import com.smart.inventory.application.exceptions.NotFoundException;
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
    private final IActivityRepository activityRepository;

    String activityTitle;

    @Autowired
    public SoldItemService(ISoldItemRepository soldItemRepository,
                           IItemRepository itemRepository,
                           IActivityRepository activityRepository) {
        this.soldItemRepository = soldItemRepository;
        this.itemRepository = itemRepository;
        this.activityRepository = activityRepository;
    }


    @Override
    public void deleteSelectedSoldItem(List<SoldItem> soldItems, Utilities utilities) {
        soldItemRepository.deleteAll(soldItems);

        utilities.configureActivity(
                activityTitle + " delete sold item:  " + soldItems,
                "",
                activityRepository);
    }

    @Override
    public void addNewSoldItem(Item items, String description, int quantity, @Nonnull Utilities utilities) {
        SoldItem soldItem = new SoldItem();
        soldItem.setItem(items);
        if(utilities.employer != null) {
            soldItem.getAddedByEmployer().add(utilities.employer);
            activityTitle = utilities.employer.getEmail() + "";
        }

        if(utilities.owner != null){
            soldItem.getAddedByOwner().add(utilities.owner);
            activityTitle = utilities.owner.getEmail();
        }
        utils(items, description, quantity, utilities, soldItem);

        utilities.configureActivity(
                activityTitle + " added sold item: " + items.getItemName(),
                "",
                activityRepository);

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
        return soldItemRepository.findById(id).orElseThrow(NotFoundException::new);
    }


    @Transactional
    @Override
    public void updateSoldItem(Integer id, String description, Item item, int quantity, Utilities utilities) {

        SoldItem soldItem = getSoldItemById(id);
        utils(item, description, quantity, utilities, soldItem);

        utilities.configureActivity(
                activityTitle + " update sold item: " + item.getItemName(),
                "",
                activityRepository);


    }

    public void utils(Item item, String description, int quantity, Utilities utilities, SoldItem soldItem) {
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
