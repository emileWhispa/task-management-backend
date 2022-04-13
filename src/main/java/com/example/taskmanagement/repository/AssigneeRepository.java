package com.example.taskmanagement.repository;

import com.example.taskmanagement.models.Assignee;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AssigneeRepository extends JpaRepository<Assignee,Long> {
}
