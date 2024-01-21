package com.pk.SimpleToDos.exception;

public enum AppErrors {
    INVALID_VALUE("Invalid value"),
    RESOURCE_NOT_FOUND("Resource not found"),
    INTERNAL_SERVER_ERROR("Internal server error"),
    DUPLICATE_FOUND("Duplicate found"),

    USER_NOT_FOUND("User not found!");

    private final String message;

    AppErrors(String message) {
        this.message = message;
    }

    public String getMessage() {
        return this.message;
    }
}
