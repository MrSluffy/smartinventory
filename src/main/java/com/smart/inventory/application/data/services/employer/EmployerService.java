package com.smart.inventory.application.data.services.employer;

import com.smart.inventory.application.data.Role;
import com.smart.inventory.application.data.entity.Company;
import com.smart.inventory.application.data.entity.Employer;
import com.smart.inventory.application.data.entity.Position;
import com.smart.inventory.application.data.repository.ICompanyRepository;
import com.smart.inventory.application.data.repository.IEmployerRepository;
import com.smart.inventory.application.data.repository.IPositionRepository;
import com.smart.inventory.application.util.Utilities;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Nonnull;
import java.util.List;

@Service
public class EmployerService implements IEmployerService{

    private final IEmployerRepository employerRepository;

    private final IPositionRepository positionRepository;

    private final ICompanyRepository companyRepository;

    @Autowired
    public EmployerService(
            IEmployerRepository employerRepository,
            IPositionRepository positionRepository,
            ICompanyRepository companyRepository) {
        this.employerRepository = employerRepository;
        this.positionRepository = positionRepository;
        this.companyRepository = companyRepository;
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
