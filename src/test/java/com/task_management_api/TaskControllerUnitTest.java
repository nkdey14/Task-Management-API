package com.task_management_api;

import com.task_management_api.controller.TaskController;
import com.task_management_api.dto.TaskRequest;
import com.task_management_api.dto.TaskResponse;
import com.task_management_api.entity.TaskStatus;
import com.task_management_api.exception.GlobalExceptionHandler;
import com.task_management_api.exception.ResourceNotFoundException;
import com.task_management_api.service.TaskService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDate;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class TaskControllerUnitTest {

    private MockMvc mockMvc;

    @Mock
    private TaskService taskService;

    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders
                .standaloneSetup(new TaskController(taskService))
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Test
    void postValidTask_returnsCreated() throws Exception {
        String due = LocalDate.now().plusDays(2).toString();
        String req = "{\"title\":\"Unit Test\",\"description\":\"desc\",\"status\":\"PENDING\",\"dueDate\":\"" + due + "\"}";

        TaskResponse resp = TaskResponse.builder()
                .id("1")
                .title("Unit Test")
                .description("desc")
                .status(TaskStatus.PENDING)
                .dueDate(LocalDate.now().plusDays(2))
                .build();

        when(taskService.createTask(any(TaskRequest.class))).thenReturn(resp);

        mockMvc.perform(post("/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(req))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value("1"))
                .andExpect(jsonPath("$.title").value("Unit Test"));
    }

    @Test
    void getTask_notFound_returns404() throws Exception {
        when(taskService.getTaskById("missing"))
                .thenThrow(new ResourceNotFoundException("Task not found with id: missing"));

        mockMvc.perform(get("/tasks/missing"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.message").value("Task not found with id: missing"));
    }

    @Test
    void putTask_notFound_returns404() throws Exception {
        String due = LocalDate.now().plusDays(3).toString();
        String req = "{\"title\":\"X\",\"description\":\"Y\",\"status\":\"PENDING\",\"dueDate\":\"" + due + "\"}";

        when(taskService.updateTask(eq("missing"), any(TaskRequest.class)))
                .thenThrow(new ResourceNotFoundException("Task not found with id: missing"));

        mockMvc.perform(put("/tasks/missing")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(req))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.message").value("Task not found with id: missing"));
    }

    @Test
    void deleteTask_notFound_returns404() throws Exception {
        doThrow(new ResourceNotFoundException("Task not found with id: missing"))
                .when(taskService).deleteTask("missing");

        mockMvc.perform(delete("/tasks/missing"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.message").value("Task not found with id: missing"));
    }
}
