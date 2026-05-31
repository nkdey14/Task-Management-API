package com.task_management_api;

import com.task_management_api.dto.TaskRequest;
import com.task_management_api.entity.TaskStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

import java.time.LocalDate;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class TaskRequestValidationTest {

    private Validator validator;

    @BeforeEach
    void setup() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void whenTitleIsBlank_thenValidationFails() {
        TaskRequest req = new TaskRequest();
        req.setTitle("");
        req.setDueDate(LocalDate.now().plusDays(1));

        Set<ConstraintViolation<TaskRequest>> violations = validator.validate(req);

        assertFalse(violations.isEmpty());
        boolean hasTitle = violations.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals("title"));
        assertTrue(hasTitle);
    }

    @Test
    void whenDueDateInPast_thenValidationFails() {
        TaskRequest req = new TaskRequest();
        req.setTitle("Test");
        req.setDueDate(LocalDate.now().minusDays(1));

        Set<ConstraintViolation<TaskRequest>> violations = validator.validate(req);

        assertFalse(violations.isEmpty());
        boolean hasDueDate = violations.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals("dueDate"));
        assertTrue(hasDueDate);
    }

    @Test
    void whenValidRequest_thenNoViolations() {
        TaskRequest req = new TaskRequest();
        req.setTitle("Valid Title");
        req.setDueDate(LocalDate.now().plusDays(5));
        req.setStatus(TaskStatus.PENDING);

        Set<ConstraintViolation<TaskRequest>> violations = validator.validate(req);

        assertTrue(violations.isEmpty());
    }
}

