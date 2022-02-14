package com.smart.inventory.application.data.config;

import com.smart.inventory.application.data.entity.*;
import com.smart.inventory.application.data.entity.ingredients.Ingredients;
import com.smart.inventory.application.data.entity.ingredients.QuantityUnit;
import com.smart.inventory.application.data.repository.*;
import com.vaadin.flow.spring.annotation.SpringComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.vaadin.artur.exampledata.DataType;
import org.vaadin.artur.exampledata.ExampleDataGenerator;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@SpringComponent
public class DataGenerator {


    private List<Employer> employers;
    private List<Company> companies;
    private List<Owner> owners;
    private List<Item> items;
    private List<Customer> customers;
    private List<Ingredients> ingredients;
    private ExampleDataGenerator<Company> companyGenerator;
    private ExampleDataGenerator<Employer> employerGenerator;
    private ExampleDataGenerator<Owner> ownerGenerator;
    private ExampleDataGenerator<Item> itemGenerator;
    private ExampleDataGenerator<Customer> buyerGenerator;
    private ExampleDataGenerator<Ingredients> costingDataGenerator;


    @Bean
    CommandLineRunner loadData(OwnerRepository ownerRepository,
                               CompanyRepository companyRepository,
                               EmployerRepository employerRepository,
                               CustomerRepository customerRepository,
                               IItemRepository IItemRepository,
                               PositionRepository positionRepository,
                               IIngredientsRepository IIngredientsRepository,
                               QuantityUnitRepository quantityUnitRepository) {

        return args -> {
            Logger logger = LoggerFactory.getLogger(getClass());
            if (employerRepository.count() != 0L) {
                logger.info("Using existing database");
                return;
            }
            int seed = 123;

            logger.info("Generating demo data");


            Random r = new Random(seed);

            List<Position> positions = positionRepository
                    .saveAll(Stream.of("Imported lead", "Not contacted", "Contacted", "Janitor", "Closed (lost)", "Dismissed")
                            .map(Position::new).collect(Collectors.toList()));

            List<QuantityUnit> quantityUnits = quantityUnitRepository.
                    saveAll(Stream.of("kilo", "pounds", "cm", "milimeter", "per each")
                            .map(QuantityUnit::new).collect(Collectors.toList()));


            costingDataGenerator = new ExampleDataGenerator<>(Ingredients.class, LocalDateTime.now());
            costingDataGenerator.setData(Ingredients::setProductName, DataType.FOOD_PRODUCT_NAME);
            costingDataGenerator.setData(Ingredients::setProductQuantity, DataType.NUMBER_UP_TO_10);
            costingDataGenerator.setData(Ingredients::setProductPrice, DataType.PRICE);

            ingredients = costingDataGenerator.create(5, seed);

            companyGenerator = new ExampleDataGenerator<>(Company.class,
                    LocalDateTime.now());
            companyGenerator.setData(Company::setName, DataType.COMPANY_NAME);
            companies = companyGenerator.create(5, seed);


            employerGenerator = new ExampleDataGenerator<>(Employer.class,
                    LocalDateTime.now());
            employerGenerator.setData(Employer::setFirstName, DataType.FIRST_NAME);
            employerGenerator.setData(Employer::setLastName, DataType.LAST_NAME);
            employerGenerator.setData(Employer::setEmail, DataType.EMAIL);
            employers = employerGenerator.create(50, seed);

            itemGenerator = new ExampleDataGenerator<>(Item.class, LocalDateTime.now());
            itemGenerator.setData(Item::setItemName, DataType.FOOD_PRODUCT_NAME);
            itemGenerator.setData(Item::setQuantity, DataType.NUMBER_UP_TO_10);
            itemGenerator.setData(Item::setPrice, DataType.PRICE);
            items = itemGenerator.create(10, seed);

            ownerGenerator = new ExampleDataGenerator<>(Owner.class,
                    LocalDateTime.now());
            ownerGenerator.setData(Owner::setFirstName, DataType.FIRST_NAME);
            ownerGenerator.setData(Owner::setLastName, DataType.LAST_NAME);
            ownerGenerator.setData(Owner::setEmail, DataType.EMAIL);

            owners = ownerGenerator.create(5, seed);

            buyerGenerator = new ExampleDataGenerator<>(Customer.class, LocalDateTime.now());
            buyerGenerator.setData(Customer::setName, DataType.FULL_NAME);
            customers = buyerGenerator.create(8, seed);

            companies.stream().map(company -> {
                company.getEmplyr().add(employers.get(r.nextInt(employers.size())));
                company.getOwnerInCompany().add(owners.get(r.nextInt(owners.size())));
                company.setOwner(owners.get(r.nextInt(owners.size())));
                return company;
            }).collect(Collectors.toList());

            employers.stream().map(employer -> {
                employer.getCompany().add(companies.get(r.nextInt(companies.size())));
                employer.setPosition(positions.get(r.nextInt(positions.size())));
                employer.setEmplyrCompany(companies.get(r.nextInt(companies.size())));
                employer.setItem(items.get(r.nextInt(items.size())));
                employer.setBuyer(customers.get(r.nextInt(customers.size())));
                return employer;
            }).collect(Collectors.toList());

            items.stream().map(item -> {
                item.getAddedBy().add(employers.get(r.nextInt(employers.size())));
                item.setTotalPrice(item.getQuantity());
                item.setDateAndTime(LocalDateTime.now().toLocalTime().toString().substring(0, 5) + "-"+ LocalDateTime.now().toLocalDate().toString());
                return item;
            }).collect(Collectors.toList());

            customers.stream().map(customer -> {
                customer.getItem().add(items.get(r.nextInt(items.size())));
                customer.setQuantity(items.get(r.nextInt(items.size())).getQuantity());
                customer.setPurchaseAmount(items.get(r.nextInt(items.size())).getPrice());
                customer.getAddedBy().add(employers.get(r.nextInt(employers.size())));
                customer.setSoldItem(items.get(r.nextInt(items.size())));
                return customer;
            }).collect(Collectors.toList());


            ingredients.stream().map(costing -> {
                costing.setTotalCost(costing.getProductQuantity());
                return costing;
            }).collect(Collectors.toList());


            quantityUnitRepository.saveAll(quantityUnits);
            IIngredientsRepository.saveAll(ingredients);
            IItemRepository.saveAll(items);
            customerRepository.saveAll(customers);
            employerRepository.saveAll(employers);
            companyRepository.saveAll(companies);
            ownerRepository.saveAll(owners);

            logger.info("Generated demo data");
        };
    }
}