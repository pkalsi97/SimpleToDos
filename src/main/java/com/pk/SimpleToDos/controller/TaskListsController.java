package com.pk.SimpleToDos.controller;

import com.pk.SimpleToDos.exception.AppException;
import com.pk.SimpleToDos.model.request.CreateTaskListRequest;
import com.pk.SimpleToDos.model.request.UpdateTaskListRequest;
import com.pk.SimpleToDos.model.responses.TaskListResponse;
import com.pk.SimpleToDos.service.TaskListsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

import static com.pk.SimpleToDos.constants.ToDoAPIConstants.*;

@RestController
public class TaskListsController {

    private static final Logger log = LoggerFactory.getLogger(TaskListsController.class);

    @Autowired
    private TaskListsService taskListsService;

    @PostMapping(API + "/" + VERSION_1 + "/" + TASKLISTS_ENDPOINT)
    public TaskListResponse createTaskList(@RequestBody CreateTaskListRequest createTaskListRequest, Principal principal) throws AppException {
        try {
            log.info("Incoming request for task list creation.");
            String username = principal.getName();
            return taskListsService.createTaskList(createTaskListRequest, username);
        } finally {
            log.info("Processing for task list creation request finished.");
        }
    }

    @PutMapping(API + "/" + VERSION_1 + "/" + TASKLISTS_ENDPOINT)
    public TaskListResponse updateTaskList(@RequestBody UpdateTaskListRequest updateTaskListRequest, Principal principal) throws AppException {
        try {
            log.info("Incoming request for task list update.");
            String username = principal.getName();
            return taskListsService.updateTaskList(updateTaskListRequest, username);
        } finally {
            log.info("Processing for task list update request finished.");
        }
    }

    @GetMapping(API + "/" + VERSION_1 + "/" + TASKLISTS_ENDPOINT + "/" + "{task_list_uuid}")
    public TaskListResponse getTaskList(@PathVariable("task_list_uuid") String taskListUuid, Principal principal) throws AppException {
        try {
            log.info("Incoming request for task list fetch.");
            String username = principal.getName();
            return taskListsService.getTaskList(taskListUuid, username);
        } finally {
            log.info("Processing for task list get request finished.");
        }
    }

    @DeleteMapping(API + "/" + VERSION_1 + "/" + TASKLISTS_ENDPOINT + "/" + "{task_list_uuid}")
    public void deleteTaskList(@PathVariable("task_list_uuid") String taskListUuid, Principal principal) throws AppException {
        try {
            log.info("Incoming request for task list delete.");
            String username = principal.getName();
            taskListsService.deleteTaskList(taskListUuid, username);
        } finally {
            log.info("Processing for task list delete request finished.");
        }
    }
}
