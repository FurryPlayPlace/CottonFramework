package net.furryplayplace.cottonframework.api.permissions.v1;

public enum PermissionDefaults {

    TRUE,
    FALSE,
    OPERATOR,
    NON_OPERATOR;

    public boolean getBooleanValue(boolean isOperator) {
        return switch (this) {
            case FALSE -> false;
            case NON_OPERATOR -> !isOperator;
            case OPERATOR -> isOperator;
            case TRUE -> true;
        };
    }
}