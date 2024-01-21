package com.pk.SimpleToDos.converters;


import com.pk.SimpleToDos.model.TaskStatus;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.util.stream.Stream;

@Converter(autoApply = true)
public class TaskStatusConverter implements AttributeConverter<TaskStatus, String> {

    @Override
    public String convertToDatabaseColumn(TaskStatus taskStatus) {
        if (taskStatus == null) {
            return null;
        }
        return taskStatus.getStatus();
    }

    @Override
    public TaskStatus convertToEntityAttribute(String taskStatus) {
        if (taskStatus == null) {
            return null;
        }
        return Stream.of(TaskStatus.values())
                .filter(c -> c.getStatus().equals(taskStatus))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }
}