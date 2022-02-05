package com.smart.inventory.application.data.config;

import com.smart.inventory.application.data.entity.*;
import com.smart.inventory.application.data.repository.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;


@Component
public class DataGenerator implements CommandLineRunner{

    private final OwnerRepository ownerRepository;
    private final CompanyRepository companyRepository;
    private final EmployerRepository employerRepository;
    private final BuyerRepository buyerRepository;
    private final ItemRepository itemRepository;
    private final PositionRepository positionRepository;

    public DataGenerator(OwnerRepository ownerRepository,
                         CompanyRepository companyRepository,
                         EmployerRepository employerRepository,
                         BuyerRepository buyerRepository,
                         ItemRepository itemRepository,
                         PositionRepository positionRepository) {
        this.ownerRepository = ownerRepository;
        this.companyRepository = companyRepository;
        this.employerRepository = employerRepository;
        this.buyerRepository = buyerRepository;
        this.itemRepository = itemRepository;
        this.positionRepository = positionRepository;
    }

    @Override
    public void run(String... args) throws Exception {


        Company company = new Company();
        Employer employer = new Employer();

        Employer employer1 = new Employer();

        Item item = new Item();


        company.setName("Murduck, Inc.");
        Position position = new Position();

        position.setPostionName("Janitor");
        if(position.postionName.isEmpty()){
            position.setPostionName("No position yet");
        }

        employer1.setFirstName("Honey");
        employer1.setLastName("Bucay");
        employer1.setEmail("c@gmail.com");
        employer1.setPosition(position);
        employer1.getCompany().add(company);
        employer1.setCompanyName(company.getName());

        employer.setFirstName("Burnok");
        employer.setLastName("Manok");
        employer.setEmail("b@gmail.com");
        employer.setPosition(position);
        employer.getCompany().add(company);
        employer.setCompanyName(company.getName());
        item.getAddedBy().add(employer);
        item.getAddedBy().add(employer1);
        company.getEmployersInCompany().add(employer);
        company.getEmployersInCompany().add(employer1);


        Owner owner = new Owner();
        owner.setFirstName("John");
        owner.setLastName("Camu");
        owner.setEmail("a@gmail.com");
        company.setOwner(owner);


        item.setItemName("Tokoyaki");
        item.setPiece(4);
        item.setPrice(40.0);
        item.setStrDate(item.dt());
        item.setTotalPrice(item.getPiece());
        employer.setItem(item);
        employer1.setItem(item);

        Buyer buyer = new Buyer();
        buyer.setName("Jessie");
        buyer.setPiece(2);
        buyer.setPrice(item.getPrice());
        buyer.setTotalPrice(buyer.getPiece());
        buyer.getItem().add(item);


        itemRepository.save(item);
        buyerRepository.save(buyer);
        positionRepository.save(position);
        employerRepository.save(employer);
        employerRepository.save(employer1);
        companyRepository.save(company);
        ownerRepository.save(owner);
    }
}
