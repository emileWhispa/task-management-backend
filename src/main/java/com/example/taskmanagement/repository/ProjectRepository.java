package com.example.taskmanagement.repository;

import com.example.taskmanagement.models.Project;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjectRepository extends JpaRepository<Project,Long> {
}
