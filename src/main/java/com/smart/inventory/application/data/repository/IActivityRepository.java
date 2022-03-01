package com.smart.inventory.application.data.repository;

import com.smart.inventory.application.data.entities.Activity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IActivityRepository extends JpaRepository<Activity, Integer> {
}
