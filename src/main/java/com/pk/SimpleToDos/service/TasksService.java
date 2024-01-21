package com.pk.SimpleToDos.service;

import com.pk.SimpleToDos.exception.AppException;
import com.pk.SimpleToDos.model.TaskLists;
import com.pk.SimpleToDos.model.TaskStatus;
import com.pk.SimpleToDos.model.Tasks;
import com.pk.SimpleToDos.model.User;
import com.pk.SimpleToDos.model.request.CreateTaskRequest;
import com.pk.SimpleToDos.model.request.UpdateTaskRequest;
import com.pk.SimpleToDos.model.responses.TaskResponse;
import com.pk.SimpleToDos.repository.TaskListsRepository;
import com.pk.SimpleToDos.repository.TasksRepository;
import com.pk.SimpleToDos.repository.UserRepository;
import com.pk.SimpleToDos.validations.tasks.CreateTaskValidator;
import com.pk.SimpleToDos.validations.tasks.GetTaskValidator;
import com.pk.SimpleToDos.validations.tasks.UpdateTaskValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

import static com.pk.SimpleToDos.exception.AppErrors.RESOURCE_NOT_FOUND;
import static com.pk.SimpleToDos.exception.AppErrors.USER_NOT_FOUND;

/**
 * Service class handling business logic for task operations.
 * Provides methods for creating, updating, retrieving, and deleting tasks.
 */
@Service
public class TasksService {

    private static final Logger log = LoggerFactory.getLogger(TasksService.class);

    @Autowired
    private TasksRepository tasksRepository;

    @Autowired
    private TaskListsRepository taskListsRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CreateTaskValidator createTaskValidator;

    @Autowired
    private GetTaskValidator getTaskValidator;

    @Autowired
    private UpdateTaskValidator updateTaskValidator;

    /**
     * Create a new task for a given task list.
     *
     * @param request  The request containing task details.
     * @param username The username of the user creating the task.
     * @return A TaskResponse containing the created task details.
     * @throws AppException If validation fails or user/task list not found.
     */
    public TaskResponse createTask(CreateTaskRequest request, String username) throws AppException {
        createTaskValidator.validate(request);

        User user = userRepository.findUsername(username)
                .orElseThrow(() -> new AppException("User not found",USER_NOT_FOUND));

        TaskLists taskList = taskListsRepository.findByIdAndUserId(UUID.fromString(request.getTaskListUuid()), user.getId())
                .orElseThrow(() -> new AppException("Task list not found",RESOURCE_NOT_FOUND));

        Tasks task = new Tasks();
        task.setUuid(UUID.randomUUID());
        task.setName(request.getName());
        task.setDescription(request.getDescription());
        task.setStatus(TaskStatus.PENDING);
        task.setTaskList(taskList);
        task.setUser(user);
        task.setCreatedAt(new Date());
        task.setUpdatedAt(new Date());

        Tasks savedTask = tasksRepository.save(task);
        return convertToTaskResponse(savedTask);
    }

    /**
     * Get details of a specific task.
     *
     * @param taskUuid The UUID of the task to retrieve.
     * @param username The username of the user retrieving the task.
     * @return A TaskResponse containing the task details.
     * @throws AppException If validation fails or the task is not found.
     */
    public TaskResponse getTask(String taskUuid, String username) throws AppException {
        getTaskValidator.validate(taskUuid);

        User user = userRepository.findUsername(username)
                .orElseThrow(() -> new AppException("User not found",USER_NOT_FOUND));

        Tasks task = tasksRepository.findByIdAndUserId(UUID.fromString(taskUuid), user.getId())
                .orElseThrow(() -> new AppException("Task not found",RESOURCE_NOT_FOUND));

        return convertToTaskResponse(task);
    }

    /**
     * Delete a specific task.
     *
     * @param taskUuid The UUID of the task to delete.
     * @param username The username of the user deleting the task.
     * @throws AppException If validation fails or the task is not found.
     */
    public void deleteTask(String taskUuid, String username) throws AppException {
        getTaskValidator.validate(taskUuid);

        User user = userRepository.findUsername(username)
                .orElseThrow(() -> new AppException("User not found",USER_NOT_FOUND));

        Tasks task = tasksRepository.findByIdAndUserId(UUID.fromString(taskUuid), user.getId())
                .orElseThrow(() -> new AppException("Task not found",RESOURCE_NOT_FOUND));

        task.setIsDeleted(true);
        task.setUpdatedAt(new Date());
        tasksRepository.save(task);
    }

    /**
     * Get all tasks for a specific task list.
     *
     * @param taskListUuid The UUID of the task list.
     * @param username     The username of the user retrieving tasks.
     * @return A list of TaskResponse containing task details.
     * @throws AppException If validation fails.
     */
    public List<TaskResponse> getTasks(String taskListUuid, String username) throws AppException {
        getTaskValidator.validate(taskListUuid);

        User user = userRepository.findUsername(username)
                .orElseThrow(() -> new AppException("User not found",USER_NOT_FOUND));

        List<Tasks> tasks = tasksRepository.findByTaskListUuidAndUserIdAndIsDeleted(UUID.fromString(taskListUuid), user.getId(), false);

        return tasks.stream().map(this::convertToTaskResponse).collect(Collectors.toList());
    }

    /**
     * Update an existing task's details.
     *
     * @param updateTaskRequest The request containing updated task details.
     * @param username          The username of the user updating the task.
     * @return A TaskResponse containing the updated task details.
     * @throws AppException If validation fails or the task is not found.
     */
    public TaskResponse updateTask(UpdateTaskRequest updateTaskRequest, String username) throws AppException {
        updateTaskValidator.validate(updateTaskRequest);

        User user = userRepository.findUsername(username)
                .orElseThrow(() -> new AppException("User not found",USER_NOT_FOUND));

        Tasks task = tasksRepository.findByIdAndUserId(UUID.fromString(updateTaskRequest.getUuid()), user.getId())
                .orElseThrow(() -> new AppException("Task not found",RESOURCE_NOT_FOUND));

        if (updateTaskRequest.getDescription() != null) {
            task.setDescription(updateTaskRequest.getDescription());
        }
        if (updateTaskRequest.getStatus() != null) {
            task.setStatus(TaskStatus.valueOf(updateTaskRequest.getStatus()));
        }
        task.setUpdatedAt(new Date());

        tasksRepository.save(task);
        return convertToTaskResponse(task);
    }

    /**
     * Converts a Tasks entity to a TaskResponse DTO.
     * This method is used to transform the task data for client-facing responses.
     *
     * @param task The Tasks entity to convert.
     * @return A TaskResponse containing the task details.
     */
    private TaskResponse convertToTaskResponse(Tasks task) {
        return TaskResponse.builder()
                .uuid(UUID.fromString(task.getUuid().toString()))
                .name(task.getName())
                .description(task.getDescription())
                .status(TaskStatus.valueOf(task.getStatus().name()))
                .taskListUuid(UUID.fromString(task.getTaskList().getUuid().toString()))
                .build();
    }
}
