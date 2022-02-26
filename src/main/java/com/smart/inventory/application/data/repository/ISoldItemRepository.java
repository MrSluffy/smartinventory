package com.smart.inventory.application.data.repository;

import com.smart.inventory.application.data.entity.SoldItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ISoldItemRepository extends JpaRepository<SoldItem, Integer> {

    @Query("select c from SoldItem c where c.soldItemCompany.id = :#{#company}")
    List<SoldItem> search(@Param("company") Integer company);

}
