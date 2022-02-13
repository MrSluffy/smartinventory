package com.smart.inventory.application.data.repository;

import com.smart.inventory.application.data.entity.costing.QuantityUnit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QuantityUnitRepository extends JpaRepository<QuantityUnit, Integer> {
}
