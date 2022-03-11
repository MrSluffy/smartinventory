package com.smart.inventory.application.data.repository;


import com.smart.inventory.application.data.entities.TodoItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ITodoItemRepository extends JpaRepository<TodoItem, String> {

    @Query("select c from TodoItem c where c.company.id = :#{#company}")
    List<TodoItem> findByCompanyId(@Param("company") Integer company);

    @Query("select c from TodoItem c where c.employer.id = :#{#employer}")
    List<TodoItem> findByEmployerId(@Param("employer") Integer employer);
}
