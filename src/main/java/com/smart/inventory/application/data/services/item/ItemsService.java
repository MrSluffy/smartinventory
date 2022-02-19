package com.smart.inventory.application.data.services.item;

import com.smart.inventory.application.data.entity.Item;
import com.smart.inventory.application.data.repository.IEmployerRepository;
import com.smart.inventory.application.data.repository.IItemRepository;
import com.smart.inventory.application.data.repository.IOwnerRepository;
import com.smart.inventory.application.util.Utilities;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ItemsService implements IItemsService{

    private final IItemRepository itemRepository;
    private final IEmployerRepository employerRepository;
    private  final IOwnerRepository ownerRepository;

    @Autowired
    public ItemsService(IItemRepository itemRepository,
                        IEmployerRepository employerRepository,
                        IOwnerRepository ownerRepository) {
        this.itemRepository = itemRepository;
        this.employerRepository = employerRepository;
        this.ownerRepository = ownerRepository;
    }


    @Override
    public List<Item> findAllItem() {
        return itemRepository.findAll();
    }

    @Override
    public List<Item> findAllItem(String filterText) {
        if(filterText == null || filterText.isEmpty()){
            return itemRepository.findAll();
        } else {
            return itemRepository.search(filterText);
        }
    }

    @Override
    public void saveItem(Item item) {

        if(Utilities.employer != null) {
            item.getAddedByEmployer().add(Utilities.employer);
        }

        if(Utilities.owner != null){
            item.getAddedByOwner().add(Utilities.owner);
        }
        item.getCompany().add(Utilities.company);
        item.setItemCompany(Utilities.company);
        itemRepository.save(item);
    }

    @Override
    public long countItems() {
        return itemRepository.count();
    }

    @Override
    public void deleteItemSelected(List<Item> item) {
        itemRepository.deleteAll(item);
    }
}
