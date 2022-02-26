package com.smart.inventory.application.data.services.employer;

import com.smart.inventory.application.data.Role;
import com.smart.inventory.application.data.entity.Company;
import com.smart.inventory.application.data.entity.Employer;
import com.smart.inventory.application.data.entity.Position;
import com.smart.inventory.application.data.repository.IEmployerRepository;
import com.smart.inventory.application.data.repository.IPositionRepository;
import com.smart.inventory.application.exeptions.NotFoundExeption;
import com.smart.inventory.application.util.Utilities;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Nonnull;
import java.util.List;

@Service
public class EmployerService implements IEmployerService{

    private final IEmployerRepository employerRepository;

    private final IPositionRepository positionRepository;

    @Autowired
    public EmployerService(
            IEmployerRepository employerRepository,
            IPositionRepository positionRepository) {
        this.employerRepository = employerRepository;
        this.positionRepository = positionRepository;
    }

    @Override
    public void deleteEmployerSelected(List<Employer> employers) {
        employerRepository.deleteAll(employers);
    }

    @Override
    public List<Employer> findAllEmployer(Integer id) {
        return employerRepository.search(id);
    }

    @Override
    public List<Position> findAllPosition() {
        return positionRepository.findAll();
    }

    public Employer getEmployerById(Integer id){
        return employerRepository.findById(id).orElseThrow(NotFoundExeption::new);
    }

    @Transactional
    @Override
    public void updateEmployer(Integer id,
                               String email,
                               String fname,
                               String lname,
                               Position position,
                               Utilities utilities) {

        var employer = getEmployerById(id);

        employer.setEmail(email);
        employer.setFirstName(fname);
        employer.setLastName(lname);
        employer.setPosition(position);

        employerRepository.save(employer);


    }

    @Override
    public void addNewEmployer(String email,
                               String firstname,
                               String lastname,
                               String password,
                               Position position,
                               @Nonnull Utilities utilities) {
        var employer = new Employer(email, firstname, lastname, password, Role.EMPLOYER);
        Company company = utilities.company;
        employer.getCompany().add(company);
        employer.setPosition(position);
        employer.setEmplyrCompany(company);
        employerRepository.save(employer);
    }
}
