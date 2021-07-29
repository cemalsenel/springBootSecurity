package com.Security.config;


public enum PersonPermission {
    USER_READ("user:read"),
    ADMIN_WRITE("admin:write"),
    ADMIN_READ("admin:read");

    private final String permission;

    private PersonPermission(String permission){
        this.permission = permission;
    }

    public String getPermission() {
        return permission;
    }
}
