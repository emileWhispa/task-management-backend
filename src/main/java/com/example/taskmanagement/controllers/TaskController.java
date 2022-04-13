package com.example.taskmanagement.controllers;

import com.example.taskmanagement.enums.ResultCodeEnum;
import com.example.taskmanagement.models.Attachment;
import com.example.taskmanagement.models.Task;
import com.example.taskmanagement.repository.AssigneeRepository;
import com.example.taskmanagement.repository.ProjectRepository;
import com.example.taskmanagement.repository.TaskRepository;
import com.example.taskmanagement.response.Result;
import com.example.taskmanagement.services.FileUploadHelper;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

@RestController
@RequestMapping("/tasks/")
public class TaskController {
    private final TaskRepository taskRepository;
    private final ProjectRepository projectRepository;
    private final AssigneeRepository assigneeRepository;

    public TaskController(TaskRepository taskRepository, ProjectRepository projectRepository, AssigneeRepository assigneeRepository) {
        this.taskRepository = taskRepository;
        this.projectRepository = projectRepository;
        this.assigneeRepository = assigneeRepository;
    }

    @GetMapping("get/values")
    public ResponseEntity<Result<Object>> getAllProjects(){

        HashMap<String, Object> hashMap = new HashMap<>();

        hashMap.put("projects",projectRepository.findAll());
        hashMap.put("assignees",assigneeRepository.findAll());

        return ResponseEntity.ok(new Result<>(hashMap));
    }
    @GetMapping("view/tasks")
    public ResponseEntity<Result<List<Task>>> getAllTasks(){
        return ResponseEntity.ok(new Result<>(taskRepository.findAll()));
    }


    @PostMapping("/action/create")
    @Transactional
    public ResponseEntity<Result<Task>> submit(
            @ModelAttribute("Task") Task task,
            @RequestParam(value = "files_attachments",required = false) MultipartFile[] files,
            @RequestParam(value = "selected_projects",required = false) List<Long> projects,
            @RequestParam(value = "selected_assignees",required = false) List<Long> assignees
            ){


        //Check if request contains valid attachment files
        if(files == null || files.length <=0 || Arrays.stream(files).anyMatch(MultipartFile::isEmpty)){
            return ResponseEntity.badRequest().body(new Result<>(ResultCodeEnum.VALIDATE_ERROR.getCode(),"No attachment files"));
        }

        //Check if valid list of assignees has been sent
        if(assignees == null || assignees.isEmpty()){
            return ResponseEntity.badRequest().body(new Result<>(ResultCodeEnum.VALIDATE_ERROR.getCode(),"No Selected assignees"));
        }
        //Check if valid non-empty list of project is available
        if(projects == null || projects.isEmpty()){
            return ResponseEntity.badRequest().body(new Result<>(ResultCodeEnum.VALIDATE_ERROR.getCode(),"No Selected projects"));
        }


        List<Attachment> attachmentList = new ArrayList<>();
        try {

            for (var file : files) {

                Attachment attachment = new Attachment();
                //Generate new filename by concatenating current time milliseconds
                String fileName = System.currentTimeMillis() + StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));

                attachment.setName(fileName);
                attachment.setTask(task);
                attachmentList.add(attachment);

                FileUploadHelper.saveFile("attachments", fileName, file);

            }
        }catch (IOException e){
            return ResponseEntity.internalServerError().body(new Result<>(ResultCodeEnum.APPLICATION_ERROR.getCode(),e.getMessage()));
        }

        //Find and assign projects and assignees to their many-to-many relationships
        task.setAssignees(assigneeRepository.findAllById(assignees));
        task.setProjects(projectRepository.findAllById(projects));
        task.setAttachments(attachmentList);


        return ResponseEntity.ok(new Result<>(taskRepository.save(task),"Task Created successfully"));
    }

}
