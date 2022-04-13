package com.example.taskmanagement.repository;

import com.example.taskmanagement.models.Task;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskRepository extends JpaRepository<Task,Long> {
}
