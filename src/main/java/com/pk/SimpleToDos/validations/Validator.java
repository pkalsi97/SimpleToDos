package com.pk.SimpleToDos.validations;

import com.pk.SimpleToDos.exception.AppException;

public interface Validator {
    public <T> void validate(T object) throws AppException;
}
