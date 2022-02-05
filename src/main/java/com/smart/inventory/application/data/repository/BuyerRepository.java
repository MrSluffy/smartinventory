package com.smart.inventory.application.data.repository;

import com.smart.inventory.application.data.entity.Buyer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BuyerRepository extends JpaRepository<Buyer, Integer> {
}
