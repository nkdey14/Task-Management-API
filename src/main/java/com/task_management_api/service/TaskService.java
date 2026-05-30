package com.task_management_api.service;

import org.springframework.data.domain.Page;

import com.task_management_api.dto.TaskRequest;
import com.task_management_api.dto.TaskResponse;
import com.task_management_api.entity.TaskStatus;

public interface TaskService {

    TaskResponse createTask(TaskRequest request);

    TaskResponse getTaskById(String id);

    TaskResponse updateTask(
            String id,
            TaskRequest request
    );

    void deleteTask(String id);

    Page<TaskResponse> getAllTasks(
            int page,
            int size,
            TaskStatus status
    );
}
