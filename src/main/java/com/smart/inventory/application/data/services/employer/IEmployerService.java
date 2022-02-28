package com.smart.inventory.application.data.services.employer;

import com.smart.inventory.application.data.entities.Employer;
import com.smart.inventory.application.data.entities.Position;
import com.smart.inventory.application.util.Utilities;

import java.util.List;

public interface IEmployerService {

    void deleteEmployerSelected(List<Employer> employers);

    void addNewEmployer(String email, String firstname, String lastname, String password, Position position, Utilities utilities);

    List<Employer> findAllEmployer(Integer id);

    List<Position> findAllPosition();

    void updateEmployer(Integer id, String email, String fname, String lname, Position position, Utilities utilities);
}
