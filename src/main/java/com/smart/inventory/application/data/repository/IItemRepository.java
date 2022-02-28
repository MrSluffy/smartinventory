package com.smart.inventory.application.data.repository;

import com.smart.inventory.application.data.entities.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IItemRepository extends JpaRepository<Item, Integer> {

    @Query("select c from Item c where c.itemCompany.id = :#{#company}")
    List<Item> search(@Param("company") Integer company);

}
