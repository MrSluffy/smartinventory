package com.smart.inventory.application.data.repository;

import com.smart.inventory.application.data.entities.Position;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IPositionRepository extends JpaRepository<Position, Integer> {

}
