package com.pk.SimpleToDos.validations.tasks;

import com.pk.SimpleToDos.exception.AppErrors;
import com.pk.SimpleToDos.exception.AppException;
import com.pk.SimpleToDos.model.request.CreateTaskRequest;
import com.pk.SimpleToDos.validations.Validator;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.UUID;

import static com.pk.SimpleToDos.constants.APIConstants.*;
import static com.pk.SimpleToDos.exception.ErrorMessages.*;

@Component
public class CreateTaskValidator implements Validator {


    @Override
    public <T> void validate(T object) throws AppException {
        CreateTaskRequest createTaskRequest = (CreateTaskRequest) object;
        validateMandatory(createTaskRequest);
        validateLengths(createTaskRequest);
        validateUuid(createTaskRequest.getTaskListUuid());
    }

    private void validateUuid(String taskListUuid) throws AppException {
        try {
            UUID isValidUUID = UUID.fromString(taskListUuid);
        } catch (IllegalArgumentException  ex) {
            throw new AppException(UUID_INVALID_MESSAGE, AppErrors.INVALID_VALUE);
        }
    }

    private void validateLengths(CreateTaskRequest createTaskRequest) throws AppException {
        if (createTaskRequest.getName().length() > MAXIMUM_LENGTH_FOR_NAME) {
            throw new AppException(NAME_MAX_LENGTH_MESSAGE, AppErrors.INVALID_VALUE);
        }

        if (Objects.nonNull(createTaskRequest.getDescription()) && createTaskRequest.getDescription().length() > MAXIMUM_LENGTH_FOR_DESCRIPTION) {
            throw new AppException(DESCRIPTION_MAX_LENGTH_MESSAGE, AppErrors.INVALID_VALUE);
        }
    }

    private void validateMandatory(CreateTaskRequest createTaskRequest) throws AppException {
        if (Objects.isNull(createTaskRequest.getName()) || createTaskRequest.getName().isBlank() || createTaskRequest.getName().isEmpty()) {
            throw new AppException(NAME_MANDATORY_FIELD_MESSAGE, AppErrors.INVALID_VALUE);
        }

        if (Objects.isNull(createTaskRequest.getDescription())) {
            throw new AppException(DESCRIPTION_MANDATORY_FIELD_MESSAGE, AppErrors.INVALID_VALUE);
        }

        if (Objects.isNull(createTaskRequest.getTaskListUuid()) || createTaskRequest.getTaskListUuid().isBlank() || createTaskRequest.getTaskListUuid().isEmpty()) {
            throw new AppException(UUID_MANDATORY_FIELD_MESSAGE, AppErrors.INVALID_VALUE);
        }
    }
}
