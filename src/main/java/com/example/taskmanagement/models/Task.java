package com.example.taskmanagement.models;

import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Getter
@Setter
public class Task extends Model{
    @Column(nullable = false)
    private String taskName;

    @DateTimeFormat(pattern = "yyy-MM-dd")
    @Column(nullable = false)
    private Date startDate;

    @DateTimeFormat(pattern = "yyy-MM-dd")
    @Column(nullable = false)
    private Date endDate;

    @Column(columnDefinition = "longtext null")
    private String description;

    @Column(nullable = false)
    private String priority;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name = "task_assignees",
            joinColumns = @JoinColumn(name = "task_id"),
            inverseJoinColumns = @JoinColumn(name = "assignee_id")
    )
    private List<Assignee> assignees;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name = "task_projects",
            joinColumns = @JoinColumn(name = "task_id"),
            inverseJoinColumns = @JoinColumn(name = "project_id")
    )
    private List<Project> projects;

    @OneToMany(mappedBy = "task",cascade = CascadeType.ALL)
    private List<Attachment> attachments = new ArrayList<>();
}
