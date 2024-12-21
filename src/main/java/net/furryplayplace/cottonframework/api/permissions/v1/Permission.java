package net.furryplayplace.cottonframework.api.permissions.v1;

import lombok.Getter;
import lombok.Setter;

public class Permission {
    private final String path;
    private final PermissionDefaults defaultValue;
    @Getter
    @Setter
    private String description;

    public Permission(String path, String description, PermissionDefaults defaultValue) {
        this.path = path;
        this.description = description;
        this.defaultValue = defaultValue;
    }

    public static Permission of(String path, String description) {
        return new Permission(path, description, PermissionDefaults.OPERATOR);
    }

    public String getPermissionAsString() {
        return path;
    }

    public PermissionDefaults getDefault() {
        return defaultValue;
    }
}