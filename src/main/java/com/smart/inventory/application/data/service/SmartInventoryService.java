package com.smart.inventory.application.data.service;

import com.smart.inventory.application.data.entity.*;
import com.smart.inventory.application.data.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SmartInventoryService {

    private CompanyRepository companyRepository;
    private BuyerRepository buyerRepository;
    private EmployerRepository employerRepository;
    private ItemRepository itemRepository;
    private OwnerRepository ownerRepository;
    private PositionRepository positionRepository;


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

}
