package com.pk.SimpleToDos.validations.tasklists;

import com.pk.SimpleToDos.exception.AppErrors;
import com.pk.SimpleToDos.exception.AppException;
import com.pk.SimpleToDos.validations.Validator;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.UUID;

import static com.pk.SimpleToDos.exception.ErrorMessages.*;

@Component
public class GetTaskListsValidator implements Validator {


    @Override
    public <T> void validate(T object) throws AppException {
        String taskListUuid = (String) object;
        validateMandatory(taskListUuid);
        validateUuid(taskListUuid);
    }

    private void validateMandatory(String taskListUuid) throws AppException {
        if (Objects.isNull(taskListUuid) || taskListUuid.isBlank() || taskListUuid.isEmpty()) {
            throw new AppException(UUID_MANDATORY_FIELD_MESSAGE, AppErrors.INVALID_VALUE);
        }
    }

    private void validateUuid(String uuid) throws AppException {
        try {
            UUID isValidUUID = UUID.fromString(uuid);
        } catch (IllegalArgumentException  ex) {
            throw new AppException(UUID_INVALID_MESSAGE, AppErrors.INVALID_VALUE);
        }
    }
}