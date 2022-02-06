package com.smart.inventory.application.data.service;

import com.smart.inventory.application.data.entity.*;
import com.smart.inventory.application.data.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SmartInventoryService {

    private final CompanyRepository companyRepository;
    private final BuyerRepository buyerRepository;
    private final EmployerRepository employerRepository;
    private final ItemRepository itemRepository;
    private final OwnerRepository ownerRepository;
    private final PositionRepository positionRepository;


    @Autowired
    public SmartInventoryService(BuyerRepository buyerRepository,
                                 CompanyRepository companyRepository,
                                 EmployerRepository employerRepository,
                                 ItemRepository itemRepository,
                                 OwnerRepository ownerRepository,
                                 PositionRepository positionRepository) {
        this.buyerRepository = buyerRepository;
        this.companyRepository = companyRepository;
        this.employerRepository = employerRepository;
        this.itemRepository = itemRepository;
        this.ownerRepository = ownerRepository;
        this.positionRepository = positionRepository;
    }

    private List<Owner> findAllOwner() {
        return ownerRepository.findAll();
    }

    private List<Company> findAllCompany(){
        return companyRepository.findAll();
    }

    private List<Employer> findAllEmployer(){
        return employerRepository.findAll();
    }

    private List<Item> findAllItem(){
        return itemRepository.findAll();
    }

    private List<Position> findAllPosition(){
        return positionRepository.findAll();
    }

    public List<Owner> findAllOwner(String filterText){
        if(filterText == null || filterText.isEmpty()){
            return ownerRepository.findAll();
        } else {
            return ownerRepository.search(filterText);
        }
    }

    public List<Item> findAllItem(String filterText){
        if(filterText == null || filterText.isEmpty()){
            return itemRepository.findAll();
        } else {
            return itemRepository.search(filterText);
        }
    }

    public long countOwner(){
        return  ownerRepository.count();
    }


    public void deletePosition(Position position){
        positionRepository.delete(position);
    }

    public void deleteOwner(Owner owner){
        ownerRepository.delete(owner);
    }

    public void saveNewOwner(Owner owner){
        ownerRepository.save(owner);
    }

    public void deleteEmployer(Employer employer){
        employerRepository.delete(employer);
    }

    public void saveNewEmployer(Employer employer){
        employerRepository.save(employer);
    }

    public CompanyRepository getCompanyRepository() {
        return companyRepository;
    }


    public BuyerRepository getBuyerRepository() {
        return buyerRepository;
    }



    public EmployerRepository getEmployerRepository() {
        return employerRepository;
    }

    public ItemRepository getItemRepository() {
        return itemRepository;
    }

    public PositionRepository getPositionRepository() {
        return positionRepository;
    }

    public void saveItem(Item item) {
        itemRepository.save(item);
    }

    public void deleteItem(Item item) {
        itemRepository.delete(item);
    }
    
    public Optional<Item> getItemById(Integer itemId){
        return itemRepository.findItemById(itemId);
    }



}
