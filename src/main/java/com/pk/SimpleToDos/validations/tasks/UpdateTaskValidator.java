package com.pk.SimpleToDos.validations.tasks;


import com.pk.SimpleToDos.exception.AppErrors;
import com.pk.SimpleToDos.exception.AppException;
import com.pk.SimpleToDos.model.TaskStatus;
import com.pk.SimpleToDos.model.request.UpdateTaskRequest;
import com.pk.SimpleToDos.validations.Validator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Objects;
import java.util.UUID;


import static com.pk.SimpleToDos.constants.ToDoAPIConstants.*;
import static com.pk.SimpleToDos.exception.ErrorMessages.*;

@Component
public class UpdateTaskValidator implements Validator {

    private static final Logger log = (Logger) LoggerFactory.getLogger(UpdateTaskValidator.class);


    @Override
    public <T> void validate(T object) throws AppException {
        UpdateTaskRequest updateTaskRequest = (UpdateTaskRequest) object;
        validateMandatory(updateTaskRequest);
        validateUuid(updateTaskRequest.getUuid());
        validateLengths(updateTaskRequest.getDescription());
        validateStatus(updateTaskRequest.getStatus());
        log.info("Received request object for task update has been validated.");
    }

    private void validateStatus(String status) throws AppException {
        if (Objects.isNull(status)) {
            return;
        }
        TaskStatus taskStatus = Arrays.stream(TaskStatus.values())
                .filter(statusValue -> statusValue.name().equals(status))
                .findFirst()
                .orElse(null);
        if (Objects.isNull(taskStatus)) {
            throw new AppException("Provided value for task status is invalid. Allowed values are COMPLETED, IN_PROGRESS or PENDING.", AppErrors. INVALID_VALUE);
        }
    }

    private void validateLengths(String description) throws AppException {
        if (Objects.nonNull(description) && description.length() > MAXIMUM_LENGTH_FOR_DESCRIPTION) {
            throw new AppException(DESCRIPTION_MAX_LENGTH_MESSAGE, AppErrors. INVALID_VALUE);
        }
    }

    private void validateUuid(String uuid) throws AppException {
        try {
            UUID isValidUUID = UUID.fromString(uuid);
        } catch (IllegalArgumentException  ex) {
            throw new AppException(UUID_INVALID_MESSAGE, AppErrors. INVALID_VALUE);
        }
    }

    private void validateMandatory(UpdateTaskRequest updateTaskRequest) throws AppException {
        if (Objects.isNull(updateTaskRequest.getUuid()) || updateTaskRequest.getUuid().isBlank() || updateTaskRequest.getUuid().isEmpty()) {
            throw new AppException(UUID_MANDATORY_FIELD_MESSAGE, AppErrors. INVALID_VALUE);
        }

        if (Objects.isNull(updateTaskRequest.getDescription()) && Objects.isNull(updateTaskRequest.getStatus())) {

            throw new AppException(MISSING_FIELD_FOR_UPDATE, AppErrors. INVALID_VALUE);
        }
    }
}
