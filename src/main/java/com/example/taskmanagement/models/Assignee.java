package com.example.taskmanagement.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;

@Entity
@Getter
@Setter
public class Assignee extends Model{

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;


    @JsonProperty
    public String getName(){
        return firstName +" "+lastName;
    }
}
