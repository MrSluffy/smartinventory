package com.smart.inventory.application.data.services.employer;

import com.smart.inventory.application.data.Role;
import com.smart.inventory.application.data.entity.Employer;
import com.smart.inventory.application.data.repository.IEmployerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmployerService implements IEmployerService{

    private final IEmployerRepository employerRepository;

    @Autowired
    public EmployerService(IEmployerRepository employerRepository) {
        this.employerRepository = employerRepository;
    }

    @Override
    public void deleteEmployerSelected(List<Employer> employers) {
        employerRepository.deleteAll(employers);
    }

    @Override
    public List<Employer> findAllEmployer(String filterText) {
        if (filterText == null || filterText.isEmpty()) {
            return employerRepository.findAll();
        } else {
            return employerRepository.search(filterText);
        }
    }

    @Override
    public void addNewEmployer(String email, String firstname, String lastname, String password) {
        employerRepository.save(new Employer(email, firstname, lastname, password, Role.EMPLOYER));
    }
}
