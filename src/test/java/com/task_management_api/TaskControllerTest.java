package com.task_management_api;


import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
class TaskControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void shouldCreateTask() throws Exception {

        String requestBody = """
        {
          "title":"Spring Boot",
          "description":"Test API",
          "status":"PENDING",
          "dueDate":"2026-06-15"
        }
        """;

        mockMvc.perform(
                        post("/tasks")
                                .contentType(
                                        MediaType.APPLICATION_JSON)
                                .content(requestBody))
                .andExpect(status().isCreated())
                .andExpect(
                        jsonPath("$.title")
                                .value("Spring Boot"));
    }
}
