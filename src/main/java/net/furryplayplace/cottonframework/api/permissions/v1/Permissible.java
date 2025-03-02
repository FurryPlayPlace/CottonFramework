package net.furryplayplace.cottonframework.api.permissions.v1;

public interface Permissible {

    public boolean h5$hasPermission(Permission id);

    public boolean h5$isHighLevelOperator();

    public void h5$setPermission(Permission id, boolean value);

}