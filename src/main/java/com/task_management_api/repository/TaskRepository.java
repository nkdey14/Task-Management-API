package com.task_management_api.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.task_management_api.entity.Task;
import com.task_management_api.entity.TaskStatus;

public interface TaskRepository extends JpaRepository<Task, String> {

    Page<Task> findByStatusOrderByDueDateAsc(
            TaskStatus status,
            Pageable pageable
    );
}