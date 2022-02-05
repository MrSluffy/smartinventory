package com.smart.inventory.application.data;

public enum Role {

    USER("customer"), ADMIN("admin");

    private String roleName;

    Role(String roleName) {
        this.roleName = roleName;
    }

    public String getRoleName() {
        return roleName;
    }
}
