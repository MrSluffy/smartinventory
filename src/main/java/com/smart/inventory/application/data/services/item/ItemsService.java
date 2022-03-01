package com.smart.inventory.application.data.services.item;

import com.smart.inventory.application.data.entities.Company;
import com.smart.inventory.application.data.entities.Item;
import com.smart.inventory.application.data.repository.*;
import com.smart.inventory.application.exceptions.NotFoundException;
import com.smart.inventory.application.util.Utilities;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Nonnull;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class ItemsService implements IItemsService{

    private final IItemRepository itemRepository;
    private final IEmployerRepository employerRepository;
    private  final IOwnerRepository ownerRepository;
    private final ICompanyRepository companyRepository;
    private final IActivityRepository activityRepository;

    String title;


    @Autowired
    public ItemsService(IItemRepository itemRepository,
                        IEmployerRepository employerRepository,
                        IOwnerRepository ownerRepository,
                        ICompanyRepository companyRepository,
                        IActivityRepository activityRepository) {
        this.itemRepository = itemRepository;
        this.employerRepository = employerRepository;
        this.ownerRepository = ownerRepository;
        this.companyRepository = companyRepository;
        this.activityRepository = activityRepository;
    }


    @Override
    public List<Item> findAllItem() {
        return itemRepository.findAll();
    }

    @Override
    public List<Item> findAllItem(Integer items) {
        return itemRepository.search(items);
    }


    @Override
    public long countItems() {
        return itemRepository.count();
    }

    @Override
    public void deleteItemSelected(List<Item> item, @Nonnull Utilities utilities) {
        itemRepository.deleteAll(item);

        utilities.configureActivity(
                title + " delete item: " + item,
                "", activityRepository);
    }

    @Override
    public void addNewItem(Item item, Utilities utilities) {
        //TODO STORE CURRENCY
        utils(item, utilities);
        itemRepository.save(item);

        utilities.configureActivity(
                title + " added item: " + item.getItemName(),
                "", activityRepository);
    }

    @Transactional
    @Override
    public void updateItem(Integer id,
                           String name,
                           int quantity,
                           Double price,
                           Double totalPrice,
                           @Nonnull Utilities utilities) {
        //TODO EDIT CURRENCY
        Item item = getItemById(id);
        item.setItemName(name);
        item.setQuantity(quantity);
        item.setPrice(price);
        item.setTotalPrice(quantity);
        itemRepository.save(item);

        utilities.configureActivity(
                title + " update item: " + name,
                "", activityRepository);
    }

    public Item getItemById(Integer id){
        return itemRepository.findById(id).orElseThrow(NotFoundException::new);
    }

    private Company getCompanyById(Integer id){
        return companyRepository.findById(id).orElseThrow(NotFoundException::new);
    }

    public void utils(Item item, @Nonnull Utilities utilities) {
        if(utilities.owner != null){
            item.getAddedByOwner().add(utilities.owner);
            title = utilities.owner.getEmail();
        }

        if(utilities.employer != null) {
            item.getAddedByEmployer().add(utilities.employer);
            title = utilities.employer.getEmail();
        }
        item.getCompany().add(utilities.company);
        item.setItemCompany(utilities.company);
        item.setDateAndTime(LocalDateTime.now().toLocalTime().toString().substring(0, 5)
                + "-" + LocalDateTime.now().toLocalDate().toString());
    }
}
