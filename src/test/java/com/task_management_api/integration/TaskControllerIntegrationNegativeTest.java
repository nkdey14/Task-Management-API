package com.task_management_api.integration;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class TaskControllerIntegrationNegativeTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void getMissingTask_returns404_andErrorBody() throws Exception {
        String id = UUID.randomUUID().toString();

        mockMvc.perform(get("/tasks/" + id))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.message").value("Task not found with id: " + id));
    }

    @Test
    void putMissingTask_returns404_andErrorBody() throws Exception {
        String id = UUID.randomUUID().toString();
        String due = LocalDate.now().plusDays(5).toString();
        String body = "{" +
                "\"title\":\"NonExistent\"," +
                "\"description\":\"x\"," +
                "\"status\":\"PENDING\"," +
                "\"dueDate\":\"" + due + "\"" +
                "}";

        mockMvc.perform(put("/tasks/" + id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.message").value("Task not found with id: " + id));
    }

    @Test
    void deleteMissingTask_returns404_andErrorBody() throws Exception {
        String id = UUID.randomUUID().toString();

        mockMvc.perform(delete("/tasks/" + id))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.message").value("Task not found with id: " + id));
    }
}

