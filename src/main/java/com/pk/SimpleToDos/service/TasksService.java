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

    public TaskResponse getTask(String taskUuid, String username) throws AppException {
        getTaskValidator.validate(taskUuid);

        User user = userRepository.findUsername(username)
                .orElseThrow(() -> new AppException("User not found",USER_NOT_FOUND));

        Tasks task = tasksRepository.findByIdAndUserId(UUID.fromString(taskUuid), user.getId())
                .orElseThrow(() -> new AppException("Task not found",RESOURCE_NOT_FOUND));

        return convertToTaskResponse(task);
    }

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

    public List<TaskResponse> getTasks(String taskListUuid, String username) throws AppException {
        getTaskValidator.validate(taskListUuid);

        User user = userRepository.findUsername(username)
                .orElseThrow(() -> new AppException("User not found",USER_NOT_FOUND));

        List<Tasks> tasks = tasksRepository.findByTaskListUuidAndUserIdAndIsDeleted(UUID.fromString(taskListUuid), user.getId(), false);

        return tasks.stream().map(this::convertToTaskResponse).collect(Collectors.toList());
    }

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
