package com.smart.inventory.application.data.services.owner;

import com.smart.inventory.application.data.entity.Owner;
import com.smart.inventory.application.exeptions.AuthException;

import java.util.Optional;

public interface IOwnerService {
    void authenticate(String email, String password, String companyName) throws AuthException;

    void register(String fname, String lname, String password1, String email, String company);

    Optional<Owner> getOwnerEmail(String value);

}
