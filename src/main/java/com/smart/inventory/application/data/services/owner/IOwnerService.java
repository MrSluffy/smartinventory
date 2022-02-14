package com.smart.inventory.application.data.services.owner;

public interface IOwnerService {
    void authenticate(String email, String password) throws OwnerService.AuthException;
}
