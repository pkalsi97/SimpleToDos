package com.pk.SimpleToDos.service;

import com.pk.SimpleToDos.exception.AppException;
import com.pk.SimpleToDos.model.TaskLists;
import com.pk.SimpleToDos.model.User;
import com.pk.SimpleToDos.model.request.CreateTaskListRequest;
import com.pk.SimpleToDos.model.request.UpdateTaskListRequest;
import com.pk.SimpleToDos.model.responses.TaskListResponse;
import com.pk.SimpleToDos.repository.TaskListsRepository;
import com.pk.SimpleToDos.repository.UserRepository;
import com.pk.SimpleToDos.validations.tasklists.CreateTaskListsValidator;
import com.pk.SimpleToDos.validations.tasklists.GetTaskListsValidator;
import com.pk.SimpleToDos.validations.tasklists.UpdateTaskListsValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.pk.SimpleToDos.exception.AppErrors.*;
import static com.pk.SimpleToDos.exception.ErrorMessages.*;

@Service
public class TaskListsService {

    private static final Logger log = LoggerFactory.getLogger(TaskListsService.class);

    @Autowired
    private TaskListsRepository taskListsRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CreateTaskListsValidator createTaskListsValidator;

    @Autowired
    private UpdateTaskListsValidator updateTaskListsValidator;

    @Autowired
    private GetTaskListsValidator getTaskListsValidator;

    public TaskListResponse createTaskList(CreateTaskListRequest request, String username) throws AppException {
        createTaskListsValidator.validate(request);
        User user = userRepository.findUsername(username)
                .orElseThrow(() -> new AppException("User not found", USER_NOT_FOUND));

        checkExistence(request.getName(), user.getId());

        TaskLists taskList = new TaskLists();
        taskList.setUuid(UUID.randomUUID());
        taskList.setName(request.getName());
        taskList.setDescription(request.getDescription());
        taskList.setCreatedAt(new Date());
        taskList.setUpdatedAt(new Date());
        taskList.setUser(user);

        TaskLists savedTaskList = taskListsRepository.save(taskList);
        return convertToTaskListResponse(savedTaskList);
    }

    public TaskListResponse updateTaskList(UpdateTaskListRequest request, String username) throws AppException {
        updateTaskListsValidator.validate(request);
        User user = userRepository.findUsername(username)
                .orElseThrow(() -> new AppException("User not found", USER_NOT_FOUND));


        TaskLists taskList = taskListsRepository.findByIdAndUserId(UUID.fromString(request.getUuid()), user.getId())
                .orElseThrow(() -> new AppException("Task list not found", RESOURCE_NOT_FOUND));

        taskList.setDescription(request.getDescription());
        taskList.setUpdatedAt(new Date());

        TaskLists updatedTaskList = taskListsRepository.save(taskList);
        return convertToTaskListResponse(updatedTaskList);
    }

    public TaskListResponse getTaskList(String taskListUuid, String username) throws AppException {
        getTaskListsValidator.validate(taskListUuid);
        User user = userRepository.findUsername(username)
                .orElseThrow(() -> new AppException("User not found", USER_NOT_FOUND));

        TaskLists taskList = taskListsRepository.findByIdAndUserId(UUID.fromString(taskListUuid), user.getId())
                .orElseThrow(() -> new AppException("Task list not found", RESOURCE_NOT_FOUND));

        return convertToTaskListResponse(taskList);
    }

    public List<TaskListResponse> getAllTaskLists(String username) throws AppException {
        User user = userRepository.findUsername(username)
                .orElseThrow(() -> new AppException("User not found", USER_NOT_FOUND));

        List<TaskLists> taskLists = taskListsRepository.findAllByUserId(user.getId());
        return taskLists.stream()
                .map(this::convertToTaskListResponse)
                .collect(Collectors.toList());
    }

    public void deleteTaskList(String taskListUuid, String username) throws AppException {
        getTaskListsValidator.validate(taskListUuid);
        User user = userRepository.findUsername(username)
                .orElseThrow(() -> new AppException("User not found", USER_NOT_FOUND));

        TaskLists taskList = taskListsRepository.findByIdAndUserId(UUID.fromString(taskListUuid), user.getId())
                .orElseThrow(() -> new AppException("Task list not found", RESOURCE_NOT_FOUND));

        taskList.setIsDeleted(true);
        taskList.setUpdatedAt(new Date());
        taskListsRepository.save(taskList);
    }

    private TaskListResponse convertToTaskListResponse(TaskLists taskList) {
        return TaskListResponse.builder()
                .uuid(UUID.fromString(taskList.getUuid().toString()))
                .name(taskList.getName())
                .description(taskList.getDescription())
                .build();
    }

    private void checkExistence(String name, Long userId) throws AppException {
        Optional<TaskLists> taskListsOptional = taskListsRepository.findByNameAndUserId(name, userId);
        if (taskListsOptional.isPresent() && !taskListsOptional.get().getIsDeleted()) {
            log.info("Task list with name : " + name + " already exist for the user. Sending error response.");
            throw new AppException(DUPLICATE_ENTRY_FOUND + name, DUPLICATE_FOUND);
        }
    }
}
