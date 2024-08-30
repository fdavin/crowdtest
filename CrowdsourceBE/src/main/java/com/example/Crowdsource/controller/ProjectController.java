package com.example.Crowdsource.controller;

import com.example.Crowdsource.model.Project;
import com.example.Crowdsource.repo.ProjectRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
public class ProjectController {

    @Autowired
    private ProjectRepo projectRepo;

    @GetMapping("/projects")
    public ResponseEntity<List<Project>> getAllProjects() {
        try {
            List<Project> projects = new ArrayList<>(projectRepo.findAll());
            if (projects.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return ResponseEntity.ok(projects);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/projects/{id}")
    public ResponseEntity<Project> getProjectByID(@PathVariable Integer id) {
        try{
            Optional<Project> projectObj = projectRepo.findById(id);
            return projectObj.map(value -> new ResponseEntity<>(value, HttpStatus.OK)).orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/projects/user/{id}")
    public ResponseEntity<List<Project>> getProjectByRequesterId(@PathVariable Integer id) {
        try{
            List<Project> projects = new ArrayList<>(projectRepo.findAll());
            List<Project> finalProjects = new ArrayList<>();
            for (Project project : projects) {
                if (project.getRequesterId() == id) {
                    finalProjects.add(project);
                }
            }
            if (finalProjects.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(finalProjects, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/projects")
    public ResponseEntity<Project> createProject(@RequestBody Project project) {
        try {
            Project projectObj = projectRepo.save(project);
            return new ResponseEntity<>(projectObj, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/projects/{id}")
    public ResponseEntity<Project> updateProject(@PathVariable Integer id, @RequestBody Project project) {
        if (projectRepo.findById(id).isPresent()) {
            Project projectObj = projectRepo.findById(id).get();
            if (project.getTitle() != null) {
                projectObj.setTitle(project.getTitle());
            }
            if (project.getDescription() != null) {
                projectObj.setDescription(project.getDescription());
            }
            projectRepo.save(projectObj);
            return new ResponseEntity<>(projectObj, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PutMapping("/projects/{projectId}/{taskId}")
    public ResponseEntity<Project> addTask(@PathVariable Integer projectId, @PathVariable Integer taskId){
        Project project = projectRepo.findById(projectId).get();
        List<Integer> taskList = project.getTaskIds();
        taskList.add(taskId);
        project.setTaskIds(taskList);
        projectRepo.save(project);
        return new ResponseEntity<Project>(project, HttpStatus.OK);
    }
}
