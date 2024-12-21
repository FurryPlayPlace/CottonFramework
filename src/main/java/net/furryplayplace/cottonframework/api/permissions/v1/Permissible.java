package net.furryplayplace.cottonframework.api.permissions.v1;

public interface Permissible {

    public boolean hasPermission(Permission id);

    public boolean isHighLevelOperator();

    public void setPermission(Permission id, boolean value);

}