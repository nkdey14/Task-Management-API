package com.task_management_api.integration;

import com.task_management_api.dto.TaskRequest;
import com.task_management_api.entity.TaskStatus;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class TaskControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void fullCrudFlow() throws Exception {
        String due1 = LocalDate.now().plusDays(10).toString();
        String createJson = "{" +
                "\"title\":\"Integration Task\"," +
                "\"description\":\"Full flow\"," +
                "\"status\":\"PENDING\"," +
                "\"dueDate\":\"" + due1 + "\"" +
                "}";

        var createResult = mockMvc.perform(post("/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(createJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").value("Integration Task"))
                .andReturn();

        String id = JsonPathUtils.extractId(createResult.getResponse().getContentAsString());

        mockMvc.perform(get("/tasks/" + id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id));

        String due2 = LocalDate.now().plusDays(20).toString();
        String updateJson = "{" +
                "\"title\":\"Integration Task Updated\"," +
                "\"description\":\"Updated\"," +
                "\"status\":\"IN_PROGRESS\"," +
                "\"dueDate\":\"" + due2 + "\"" +
                "}";

        mockMvc.perform(put("/tasks/" + id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updateJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Integration Task Updated"));

        mockMvc.perform(delete("/tasks/" + id))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/tasks/" + id))
                .andExpect(status().isNotFound());
    }
}
