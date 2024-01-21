package com.pk.SimpleToDos.model.responses;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.pk.SimpleToDos.model.TaskStatus;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@Builder
public class TaskResponse {

    @JsonProperty("uuid")
    private UUID uuid;

    @JsonProperty("name")
    private String name;

    @JsonProperty("description")
    private String description;

    @JsonProperty("status")
    private TaskStatus status;

    @JsonProperty("tasklist_uuid")
    private UUID taskListUuid;
}
