package com.task_management_api;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalDate;
import java.util.Optional;
import java.util.List;

import com.task_management_api.dto.TaskRequest;
import com.task_management_api.dto.TaskResponse;
import com.task_management_api.entity.Task;
import com.task_management_api.entity.TaskStatus;
import com.task_management_api.exception.ResourceNotFoundException;
import com.task_management_api.repository.TaskRepository;
import com.task_management_api.service.impl.TaskServiceImpl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

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

    @Test
    void shouldUpdateTask() {
        Task existing = Task.builder()
                .id("1")
                .title("Old Title")
                .description("Old desc")
                .status(TaskStatus.PENDING)
                .dueDate(LocalDate.now().plusDays(3))
                .build();

        TaskRequest req = new TaskRequest();
        req.setTitle("New Title");
        req.setDescription("New Desc");
        req.setStatus(TaskStatus.IN_PROGRESS);
        req.setDueDate(LocalDate.now().plusDays(7));

        Task updated = Task.builder()
                .id("1")
                .title(req.getTitle())
                .description(req.getDescription())
                .status(req.getStatus())
                .dueDate(req.getDueDate())
                .build();

        when(repository.findById("1")).thenReturn(Optional.of(existing));
        when(repository.save(any(Task.class))).thenReturn(updated);

        TaskResponse resp = service.updateTask("1", req);

        assertNotNull(resp);
        assertEquals("1", resp.getId());
        assertEquals("New Title", resp.getTitle());
        assertEquals(TaskStatus.IN_PROGRESS, resp.getStatus());

        verify(repository, times(1)).findById("1");
        verify(repository, times(1)).save(any(Task.class));
    }

    @Test
    void shouldDeleteTask() {
        Task existing = Task.builder()
                .id("1")
                .title("To Delete")
                .status(TaskStatus.PENDING)
                .dueDate(LocalDate.now().plusDays(2))
                .build();

        when(repository.findById("1")).thenReturn(Optional.of(existing));

        service.deleteTask("1");

        verify(repository, times(1)).findById("1");
        verify(repository, times(1)).delete(existing);
    }

    @Test
    void shouldGetAllTasksWithoutStatus() {
        Task t1 = Task.builder()
                .id("1")
                .title("A")
                .status(TaskStatus.PENDING)
                .dueDate(LocalDate.now().plusDays(3))
                .build();

        Task t2 = Task.builder()
                .id("2")
                .title("B")
                .status(TaskStatus.IN_PROGRESS)
                .dueDate(LocalDate.now().plusDays(5))
                .build();

        Page<Task> page = new PageImpl<>(List.of(t1, t2));

        when(repository.findAll(any(Pageable.class))).thenReturn(page);

        Page<TaskResponse> result = service.getAllTasks(0, 10, null);

        assertEquals(2, result.getTotalElements());
        assertEquals("1", result.getContent().get(0).getId());

        verify(repository, times(1)).findAll(any(Pageable.class));
    }

    @Test
    void shouldGetAllTasksWithStatus() {
        Task t1 = Task.builder()
                .id("1")
                .title("A")
                .status(TaskStatus.PENDING)
                .dueDate(LocalDate.now().plusDays(3))
                .build();

        Page<Task> page = new PageImpl<>(List.of(t1));

        when(repository.findByStatusOrderByDueDateAsc(eq(TaskStatus.PENDING), any(Pageable.class)))
                .thenReturn(page);

        Page<TaskResponse> result = service.getAllTasks(0, 10, TaskStatus.PENDING);

        assertEquals(1, result.getTotalElements());
        assertEquals(TaskStatus.PENDING, result.getContent().get(0).getStatus());

        verify(repository, times(1)).findByStatusOrderByDueDateAsc(eq(TaskStatus.PENDING), any(Pageable.class));
    }

    // Negative / edge-case tests
    @Test
    void getTaskById_notFound_throwsResourceNotFound() {
        when(repository.findById("missing")).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> service.getTaskById("missing"));

        verify(repository, times(1)).findById("missing");
    }

    @Test
    void updateTask_notFound_throwsResourceNotFound() {
        TaskRequest req = new TaskRequest();
        req.setTitle("X");
        req.setDueDate(LocalDate.now().plusDays(1));

        when(repository.findById("missing")).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> service.updateTask("missing", req));

        verify(repository, times(1)).findById("missing");
    }

    @Test
    void deleteTask_notFound_throwsResourceNotFound() {
        when(repository.findById("missing")).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> service.deleteTask("missing"));

        verify(repository, times(1)).findById("missing");
    }
}