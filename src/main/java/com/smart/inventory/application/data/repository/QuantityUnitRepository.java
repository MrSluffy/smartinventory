package com.smart.inventory.application.data.repository;

import com.smart.inventory.application.data.entity.ingredients.QuantityUnit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QuantityUnitRepository extends JpaRepository<QuantityUnit, Integer> {
}
