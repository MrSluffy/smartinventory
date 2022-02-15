package com.smart.inventory.application.data.services.owner;

import com.smart.inventory.application.exeptions.AuthException;

public interface IOwnerService {
    void authenticate(String email, String password) throws AuthException;
}
