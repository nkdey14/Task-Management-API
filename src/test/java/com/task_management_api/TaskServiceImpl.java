package com.task_management_api;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalDate;
import java.util.Optional;

import com.task_management_api.dto.TaskRequest;
import com.task_management_api.dto.TaskResponse;
import com.task_management_api.entity.Task;
import com.task_management_api.entity.TaskStatus;
import com.task_management_api.repository.TaskRepository;
import com.task_management_api.service.impl.TaskServiceImpl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class TaskServiceImplTest {

    @Mock
    private TaskRepository repository;

    @InjectMocks
    private TaskServiceImpl service;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldCreateTask() {

        TaskRequest request = new TaskRequest();
        request.setTitle("Learn Spring Boot");
        request.setDescription("Practice Project");
        request.setStatus(TaskStatus.PENDING);
        request.setDueDate(LocalDate.now().plusDays(5));

        Task task = Task.builder()
                .id("1")
                .title(request.getTitle())
                .description(request.getDescription())
                .status(request.getStatus())
                .dueDate(request.getDueDate())
                .build();

        when(repository.save(any(Task.class)))
                .thenReturn(task);

        TaskResponse response =
                service.createTask(request);

        assertNotNull(response);
        assertEquals("1", response.getId());
        assertEquals("Learn Spring Boot",
                response.getTitle());

        verify(repository, times(1))
                .save(any(Task.class));
    }

    @Test
    void shouldGetTaskById() {

        Task task = Task.builder()
                .id("1")
                .title("Task")
                .status(TaskStatus.PENDING)
                .dueDate(LocalDate.now().plusDays(2))
                .build();

        when(repository.findById("1"))
                .thenReturn(Optional.of(task));

        TaskResponse response =
                service.getTaskById("1");

        assertEquals("1", response.getId());
    }
}