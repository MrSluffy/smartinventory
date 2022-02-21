package com.smart.inventory.application.data.repository;

import com.smart.inventory.application.data.entity.ingredients.Ingredients;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IIngredientsRepository extends JpaRepository<Ingredients, Integer> {

    @Query("select c from Ingredients c where c.ingredientCompany.id = :#{#company}")
    List<Ingredients> search(@Param("company") Integer company);

}
