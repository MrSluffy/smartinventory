package com.smart.inventory.application.data.repository;

import com.smart.inventory.application.data.entities.Activity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IActivityRepository extends JpaRepository<Activity, Integer> {

    @Query("select c from Activity c " +
            "where c.company.id = :#{#company} ")
    List<Activity> search(@Param("company") Integer company);
}
