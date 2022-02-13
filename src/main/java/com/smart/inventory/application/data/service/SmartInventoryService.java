package com.smart.inventory.application.data.service;

import com.smart.inventory.application.data.entity.*;
import com.smart.inventory.application.data.entity.costing.Ingredients;
import com.smart.inventory.application.data.entity.costing.QuantityUnit;
import com.smart.inventory.application.data.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Nonnull;
import java.util.List;

@Service
public class SmartInventoryService {

    private final CompanyRepository companyRepository;
    private final CustomerRepository customerRepository;
    private final EmployerRepository employerRepository;
    private final ItemRepository itemRepository;
    private final OwnerRepository ownerRepository;
    private final PositionRepository positionRepository;
    private final IngredientsRepository ingredientsRepository;
    private final QuantityUnitRepository quantityUnitRepository;

    @Autowired
    public SmartInventoryService(CustomerRepository customerRepository,
                                 CompanyRepository companyRepository,
                                 EmployerRepository employerRepository,
                                 ItemRepository itemRepository,
                                 OwnerRepository ownerRepository,
                                 PositionRepository positionRepository,
                                 IngredientsRepository ingredientsRepository,
                                 QuantityUnitRepository quantityUnitRepository) {
        this.customerRepository = customerRepository;
        this.companyRepository = companyRepository;
        this.employerRepository = employerRepository;
        this.itemRepository = itemRepository;
        this.ownerRepository = ownerRepository;
        this.positionRepository = positionRepository;
        this.ingredientsRepository = ingredientsRepository;
        this.quantityUnitRepository = quantityUnitRepository;
    }

    public List<Owner> findAllOwner() {
        return ownerRepository.findAll();
    }

    public List<Company> findAllCompany(){
        return companyRepository.findAll();
    }

    public List<Employer> findAllEmployer(){
        return employerRepository.findAll();
    }

    public List<Item> findAllItem(){
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

    public List<Ingredients> findAllCosting(String filterText){
        if(filterText == null || filterText.isEmpty()){
            return ingredientsRepository.findAll();
        } else {
            return ingredientsRepository.search(filterText);
        }
    }

    public long countOwner(){
        return  ownerRepository.count();
    }

    public long countItems(){
        return  itemRepository.count();
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


    public CustomerRepository getCustomerRepository() {
        return customerRepository;
    }


    public IngredientsRepository getCostingRepository(){
        return ingredientsRepository;
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

    public void deleteItemSelected(List<Item> item) {
        itemRepository.deleteAll(item);
    }

    public void deleteIngredientSelected(List<Ingredients> ingredients) {
        ingredientsRepository.deleteAll(ingredients);
    }

    public void saveIngredient(@Nonnull Ingredients ingredients) {
        ingredientsRepository.save(ingredients);
    }

    public List<QuantityUnit> findAllUnit() {
        return quantityUnitRepository.findAll();
    }
}
