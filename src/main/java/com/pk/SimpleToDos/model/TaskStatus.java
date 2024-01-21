package com.pk.SimpleToDos.model;
import lombok.*;


public enum TaskStatus {
    PENDING("Pending"),
    IN_PROGRESS("In Progress"),
    COMPLETED("Completed");

    private final String status;

    TaskStatus(String status) {
        this.status = status;
    }
    public String getStatus() {
        return status;
    }
}
