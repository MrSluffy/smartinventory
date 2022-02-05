package com.smart.inventory.application.data.repository;

import com.smart.inventory.application.data.entity.Position;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PositionRepository extends JpaRepository<Position, Integer> {

}
