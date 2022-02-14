package com.smart.inventory.application.data.repository;

import com.smart.inventory.application.data.entity.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IItemRepository extends JpaRepository<Item, Integer> {

    @Query("select c from Item c " +
            "where lower(c.itemName) like lower(concat('%', :searchTerm, '%'))")
    List<Item> search(@Param("searchTerm") String searchTerm);

    Optional<Item> findItemById(Integer id);

}