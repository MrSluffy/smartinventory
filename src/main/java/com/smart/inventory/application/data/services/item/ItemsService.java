package com.smart.inventory.application.data.services.item;

import com.smart.inventory.application.data.entity.Item;
import com.smart.inventory.application.data.repository.IEmployerRepository;
import com.smart.inventory.application.data.repository.IItemRepository;
import com.smart.inventory.application.data.repository.IOwnerRepository;
import com.smart.inventory.application.util.Utilities;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Nonnull;
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
        if(findAllItem().isEmpty()){
            return null;
        }
        if(filterText == null || filterText.isEmpty()){
            return itemRepository.findAll();
        } else {
            return itemRepository.search(filterText);
        }
    }

    @Override
    public void saveItem(Item item, @Nonnull Utilities utilities) {
        if(utilities.employer != null) {
            item.getAddedByEmployer().add(utilities.employer);
        }

        if(utilities.owner != null){
            item.getAddedByOwner().add(utilities.owner);
        }
        item.getCompany().add(utilities.company);
        item.setItemCompany(utilities.company);
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
