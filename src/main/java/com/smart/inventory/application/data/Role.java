package com.smart.inventory.application.data;

public enum Role {

    EMPLOYER("employer"),CMP_OWNER("owner"), ADMIN("admin");

    private final String roleName;

    Role(String roleName) {
        this.roleName = roleName;
    }

    public String getRoleName() {
        return roleName;
    }
}
