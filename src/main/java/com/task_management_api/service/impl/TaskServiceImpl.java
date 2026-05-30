package com.task_management_api.service.impl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.task_management_api.dto.TaskRequest;
import com.task_management_api.dto.TaskResponse;
import com.task_management_api.entity.Task;
import com.task_management_api.entity.TaskStatus;
import com.task_management_api.exception.ResourceNotFoundException;
import com.task_management_api.repository.TaskRepository;
import com.task_management_api.service.TaskService;

@Service
public class TaskServiceImpl implements TaskService {

    private final TaskRepository repository;

    public TaskServiceImpl(TaskRepository repository) {
        this.repository = repository;
    }

    @Override
    public TaskResponse createTask(TaskRequest request) {

        Task task = Task.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .status(request.getStatus())
                .dueDate(request.getDueDate())
                .build();

        Task savedTask = repository.save(task);

        return mapToResponse(savedTask);
    }

    @Override
    public TaskResponse getTaskById(String id) {

        Task task = repository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Task not found with id: " + id
                        ));

        return mapToResponse(task);
    }

    @Override
    public TaskResponse updateTask(
            String id,
            TaskRequest request) {

        Task task = repository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Task not found with id: " + id
                        ));

        task.setTitle(request.getTitle());
        task.setDescription(request.getDescription());
        task.setStatus(request.getStatus());
        task.setDueDate(request.getDueDate());

        Task updatedTask = repository.save(task);

        return mapToResponse(updatedTask);
    }

    @Override
    public void deleteTask(String id) {

        Task task = repository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Task not found with id: " + id
                        ));

        repository.delete(task);
    }

    @Override
    public Page<TaskResponse> getAllTasks(
            int page,
            int size,
            TaskStatus status) {

        Pageable pageable = PageRequest.of(
                page,
                size,
                Sort.by("dueDate").ascending()
        );

        Page<Task> tasks;

        if (status != null) {

            tasks = repository.findByStatusOrderByDueDateAsc(
                    status,
                    pageable
            );

        } else {

            tasks = repository.findAll(pageable);
        }

        return tasks.map(this::mapToResponse);
    }

    private TaskResponse mapToResponse(Task task) {

        return TaskResponse.builder()
                .id(task.getId())
                .title(task.getTitle())
                .description(task.getDescription())
                .status(task.getStatus())
                .dueDate(task.getDueDate())
                .build();
    }
}