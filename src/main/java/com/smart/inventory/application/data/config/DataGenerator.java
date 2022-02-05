package com.smart.inventory.application.data.config;

import com.smart.inventory.application.data.entity.*;
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
    private List<Buyer> buyers;
    private ExampleDataGenerator<Company> companyGenerator;
    private ExampleDataGenerator<Employer> employerGenerator;
    private ExampleDataGenerator<Owner> ownerGenerator;
    private ExampleDataGenerator<Item> itemGenerator;
    private ExampleDataGenerator<Buyer> buyerGenerator;


    @Bean
    public CommandLineRunner loadData(OwnerRepository ownerRepository,
                                      CompanyRepository companyRepository,
                                      EmployerRepository employerRepository,
                                      BuyerRepository buyerRepository,
                                      ItemRepository itemRepository,
                                      PositionRepository positionRepository) {

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
            itemGenerator.setData(Item::setPiece, DataType.NUMBER_UP_TO_10);
            itemGenerator.setData(Item::setPrice, DataType.PRICE);
            items = itemGenerator.create(10, seed);

            ownerGenerator = new ExampleDataGenerator<>(Owner.class,
                    LocalDateTime.now());
            ownerGenerator.setData(Owner::setFirstName, DataType.FIRST_NAME);
            ownerGenerator.setData(Owner::setLastName, DataType.LAST_NAME);
            ownerGenerator.setData(Owner::setEmail, DataType.EMAIL);

            owners = ownerGenerator.create(5, seed);

            buyerGenerator = new ExampleDataGenerator<>(Buyer.class, LocalDateTime.now());
            buyerGenerator.setData(Buyer::setName, DataType.FULL_NAME);
            buyers = buyerGenerator.create(8, seed);

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
                employer.setBuyer(buyers.get(r.nextInt(buyers.size())));
                return employer;
            }).collect(Collectors.toList());

            items.stream().map(item -> {
                item.getAddedBy().add(employers.get(r.nextInt(employers.size())));
                item.setTotalPrice(item.getPiece());
                item.setStrDate(LocalDateTime.now().toString());
                item.setBuyer(buyers.get(r.nextInt(items.size())));
                return item;
            }).collect(Collectors.toList());

            buyers.stream().map(buyer -> {
                buyer.getItem().add(items.get(r.nextInt(items.size())));
                buyer.setPiece(items.get(r.nextInt(items.size())).getPiece());
                buyer.setPrice(items.get(r.nextInt(items.size())).getPrice());
                buyer.setTotalPrice(items.get(r.nextInt(items.size())).getTotalPrice());
                buyer.getAddedBy().add(employers.get(r.nextInt(employers.size())));
                buyer.setSoldItem(items.get(r.nextInt(items.size())));
                return buyer;
            }).collect(Collectors.toList());




            itemRepository.saveAll(items);
            buyerRepository.saveAll(buyers);
            employerRepository.saveAll(employers);
            companyRepository.saveAll(companies);
            ownerRepository.saveAll(owners);

            logger.info("Generated demo data");
        };
    }
}