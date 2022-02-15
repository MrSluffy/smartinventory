package com.smart.inventory.application.data.services.employer;

import com.smart.inventory.application.data.entity.Employer;
import com.smart.inventory.application.data.entity.Position;

import java.util.List;

public interface IEmployerService {

    void deleteEmployerSelected(List<Employer> employers);

    void addNewEmployer(String email, String firstname, String lastname, String password, Position position);

    List<Employer> findAllEmployer(String filterText);

    List<Position> findAllPosition();
}
