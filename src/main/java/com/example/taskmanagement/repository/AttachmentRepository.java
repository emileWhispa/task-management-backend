package com.example.taskmanagement.repository;

import com.example.taskmanagement.models.Attachment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AttachmentRepository extends JpaRepository<Attachment,Long> {
}
