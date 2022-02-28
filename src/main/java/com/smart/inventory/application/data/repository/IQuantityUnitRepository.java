package com.smart.inventory.application.data.repository;

import com.smart.inventory.application.data.entities.ingredients.QuantityUnit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IQuantityUnitRepository extends JpaRepository<QuantityUnit, Integer> {
}
