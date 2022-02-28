package com.smart.inventory.application.data.services.item;

import com.smart.inventory.application.data.entities.Company;
import com.smart.inventory.application.data.entities.Item;
import com.smart.inventory.application.data.repository.ICompanyRepository;
import com.smart.inventory.application.data.repository.IEmployerRepository;
import com.smart.inventory.application.data.repository.IItemRepository;
import com.smart.inventory.application.data.repository.IOwnerRepository;
import com.smart.inventory.application.exeptions.NotFoundExeption;
import com.smart.inventory.application.util.Utilities;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ItemsService implements IItemsService{

    private final IItemRepository itemRepository;
    private final IEmployerRepository employerRepository;
    private  final IOwnerRepository ownerRepository;
    private final ICompanyRepository companyRepository;

    @Autowired
    public ItemsService(IItemRepository itemRepository,
                        IEmployerRepository employerRepository,
                        IOwnerRepository ownerRepository,
                        ICompanyRepository companyRepository) {
        this.itemRepository = itemRepository;
        this.employerRepository = employerRepository;
        this.ownerRepository = ownerRepository;
        this.companyRepository = companyRepository;
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
    public void deleteItemSelected(List<Item> item) {
        itemRepository.deleteAll(item);
    }

    @Override
    public void addNewItem(Item item, Utilities utilities) {
        //TODO STORE CURRENCY
        utils(item, utilities);
        itemRepository.save(item);
    }

    @Transactional
    @Override
    public void updateItem(Integer id,
                           String name,
                           int quantity,
                           Double price,
                           Double totalPrice,
                           Utilities utilities) {
        //TODO EDIT CURRENCY
        Item item = getItemById(id);
        item.setItemName(name);
        item.setQuantity(quantity);
        item.setPrice(price);
        item.setTotalPrice(quantity);
        itemRepository.save(item);
    }

    public Item getItemById(Integer id){
        return itemRepository.findById(id).orElseThrow(NotFoundExeption::new);
    }

    private Company getCompanyById(Integer id){
        return companyRepository.findById(id).orElseThrow(NotFoundExeption::new);
    }

    public void utils(Item item, Utilities utilities) {
        if(utilities.employer != null) {
            item.getAddedByEmployer().add(utilities.employer);
        }

        if(utilities.owner != null){
            item.getAddedByOwner().add(utilities.owner);
        }
        item.getCompany().add(utilities.company);
        item.setItemCompany(utilities.company);
        item.setDateAndTime(LocalDateTime.now().toLocalTime().toString().substring(0, 5)
                + "-" + LocalDateTime.now().toLocalDate().toString());
    }
}
