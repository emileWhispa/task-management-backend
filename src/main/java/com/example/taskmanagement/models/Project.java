package com.example.taskmanagement.models;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;

@Entity
@Getter
@Setter
public class Project extends Model{
    @Column(nullable = false)
    private String title;
    private String description;

}
