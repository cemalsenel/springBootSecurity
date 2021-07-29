package com.Security.config;


public enum PersonPermission {
    USER_READ("user:read"),
    ADMIN_WRITE("admin:read"),
    ADMIN_READ("admin:write");

    private final String permission;

    private PersonPermission(String permission){
        this.permission = permission;
    }

    public String getPermission() {
        return permission;
    }
}
