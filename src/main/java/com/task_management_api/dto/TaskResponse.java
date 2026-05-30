package com.task_management_api.dto;

import java.time.LocalDate;

import com.task_management_api.entity.TaskStatus;

public class TaskResponse {

    private String id;
    private String title;
    private String description;
    private TaskStatus status;
    private LocalDate dueDate;

    public TaskResponse() {
    }

    public TaskResponse(String id, String title, String description, TaskStatus status, LocalDate dueDate) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.status = status;
        this.dueDate = dueDate;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public TaskStatus getStatus() {
        return status;
    }

    public void setStatus(TaskStatus status) {
        this.status = status;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    // Simple builder implementation
    public static TaskResponseBuilder builder() {
        return new TaskResponseBuilder();
    }

    public static class TaskResponseBuilder {
        private String id;
        private String title;
        private String description;
        private TaskStatus status;
        private LocalDate dueDate;

        public TaskResponseBuilder id(String id) {
            this.id = id;
            return this;
        }

        public TaskResponseBuilder title(String title) {
            this.title = title;
            return this;
        }

        public TaskResponseBuilder description(String description) {
            this.description = description;
            return this;
        }

        public TaskResponseBuilder status(TaskStatus status) {
            this.status = status;
            return this;
        }

        public TaskResponseBuilder dueDate(LocalDate dueDate) {
            this.dueDate = dueDate;
            return this;
        }

        public TaskResponse build() {
            return new TaskResponse(id, title, description, status, dueDate);
        }
    }
}
