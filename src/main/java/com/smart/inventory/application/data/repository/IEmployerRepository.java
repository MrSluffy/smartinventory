package com.smart.inventory.application.data.repository;

import com.smart.inventory.application.data.entities.Employer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IEmployerRepository extends JpaRepository<Employer, Integer> {

    @Query("select c from Employer c " +
            "where c.emplyrCompany.id = :#{#company} ")
    List<Employer> search(@Param("company") Integer company);

    Employer getByEmail(String email);
}
