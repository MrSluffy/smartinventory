package com.smart.inventory.application.data.services.item;

import com.smart.inventory.application.data.entity.Company;
import com.smart.inventory.application.data.entity.Employer;
import com.smart.inventory.application.data.entity.Item;
import com.smart.inventory.application.data.entity.Owner;
import com.smart.inventory.application.data.repository.IEmployerRepository;
import com.smart.inventory.application.data.repository.IItemRepository;
import com.smart.inventory.application.data.repository.IOwnerRepository;
import com.vaadin.flow.server.VaadinSession;
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
        var employer = VaadinSession.getCurrent().getAttribute(Employer.class);
        var owner = VaadinSession.getCurrent().getAttribute(Owner.class);
        var company = VaadinSession.getCurrent().getAttribute(Company.class);
        if(employer != null) {
            item.getAddedByEmployer().add(employer);
        }

        if(owner != null){
            item.getAddedByOwner().add(owner);
        }
        item.getCompany().add(company);
        item.setItemCompany(company);
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
