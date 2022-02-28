package com.smart.inventory.application.data.repository;

import com.smart.inventory.application.data.entities.Owner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IOwnerRepository extends JpaRepository<Owner, Integer> {

    Optional<Owner> findOwnerByEmail(String value);

    Owner getByEmail(String email);
}
