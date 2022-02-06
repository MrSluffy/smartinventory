package com.smart.inventory.application.data.repository;

import com.smart.inventory.application.data.entity.Employer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployerRepository extends JpaRepository<Employer, Integer> {
}
