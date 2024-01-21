package com.pk.SimpleToDos.controller;

import com.pk.SimpleToDos.exception.AppException;
import com.pk.SimpleToDos.model.request.CreateTaskRequest;
import com.pk.SimpleToDos.model.request.UpdateTaskRequest;
import com.pk.SimpleToDos.model.responses.TaskResponse;
import com.pk.SimpleToDos.service.TasksService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

import static com.pk.SimpleToDos.constants.ToDoAPIConstants.*;


@RestController
public class TasksController {

    private static final Logger log = LoggerFactory.getLogger(TasksController.class);

    @Autowired
    private TasksService tasksService;

    @PostMapping(API + "/" + VERSION_1 + "/" + TASKS_ENDPOINT)
    public TaskResponse createTask(@RequestBody CreateTaskRequest createTaskRequest, Principal principal) throws AppException {
        try {
            log.info("Incoming request for task creation.");
            String username = principal.getName();
            return tasksService.createTask(createTaskRequest, username);
        } finally {
            log.info("Processing for task creation request finished.");
        }
    }

    @PutMapping(API + "/" + VERSION_1 + "/" + TASKS_ENDPOINT)
    public TaskResponse updateTask(@RequestBody UpdateTaskRequest updateTaskRequest, Principal principal) throws AppException {
        try {
            log.info("Incoming request for task update.");
            String username = principal.getName();
            return tasksService.updateTask(updateTaskRequest, username);
        } finally {
            log.info("Processing for task update request finished.");
        }
    }

    @GetMapping(API + "/" + VERSION_1 + "/" + TASKS_ENDPOINT + "/" + "{task_uuid}")
    public TaskResponse fetchTask(@PathVariable("task_uuid") String taskUuid, Principal principal) throws AppException {
        try {
            log.info("Incoming request for task fetch.");
            String username = principal.getName();
            return tasksService.getTask(taskUuid, username);
        } finally {
            log.info("Processing for task fetch request finished.");
        }
    }

    @DeleteMapping(API + "/" + VERSION_1 + "/" + TASKS_ENDPOINT + "/" + "{task_uuid}")
    public void deleteTask(@PathVariable("task_uuid") String taskUuid, Principal principal) throws AppException {
        try {
            log.info("Incoming request for task delete.");
            String username = principal.getName();
            tasksService.deleteTask(taskUuid, username);
        } finally {
            log.info("Processing for task delete request finished.");
        }
    }

    @GetMapping(API + "/" + VERSION_1 + "/" + TASKS_ENDPOINT + "/" + TASKLIST_ENDPOINT + "/" + "{tasklist_uuid}")
    public List<TaskResponse> fetchTasks(@PathVariable("tasklist_uuid") String taskListUuid, Principal principal) throws AppException {
        try {
            log.info("Incoming request for tasks fetch.");
            String username = principal.getName();
            return tasksService.getTasks(taskListUuid, username);
        } finally {
            log.info("Processing for task fetch request finished.");
        }
    }
}
