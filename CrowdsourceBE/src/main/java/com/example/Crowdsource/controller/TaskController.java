package com.example.Crowdsource.controller;

import com.example.Crowdsource.model.File;
import com.example.Crowdsource.model.Project;
import com.example.Crowdsource.model.Task;
import com.example.Crowdsource.model.TestCase;
import com.example.Crowdsource.repo.FileRepo;
import com.example.Crowdsource.repo.TaskRepo;
import com.example.Crowdsource.controller.ProjectController;
import com.example.Crowdsource.repo.TestCaseRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
public class TaskController {
    @Autowired
    private TaskRepo taskRepo;
    @Autowired
    ProjectController projectController = new ProjectController();
    @Autowired
    private FileRepo fileRepo;
    @Autowired
    private TestCaseRepo testCaseRepo;
    @GetMapping("/tasks")
    public ResponseEntity<List<Task>> getAllTasks() {
        try {
            List<Task> tasks = new ArrayList<>(taskRepo.findAll());
            if (tasks.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return ResponseEntity.ok(tasks);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @GetMapping("/task/{id}")
    public ResponseEntity<Task> getTask(@PathVariable Integer id) {
        try {
            Optional<Task> task = taskRepo.findById(id);
            return task.map(value -> new ResponseEntity<>(value, HttpStatus.OK)).orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
        }catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/task/upload/{id}")
    public ResponseEntity<File> uploadFile(@RequestParam("file") MultipartFile file, @PathVariable Integer id){
        try{
            File fileEntity = new File();
            fileEntity.setId(id);
            fileEntity.setFileType("executable");
            fileEntity.setData(file.getBytes());
            fileRepo.save(fileEntity);
            return new ResponseEntity<>(fileEntity, HttpStatus.CREATED);
        }catch (Exception e){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("task/download/{id}")
    public ResponseEntity<byte[]> downloadFile(@PathVariable Integer id) {
        try{
            File file = fileRepo.findSpecificFile(id,"executable");
            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filetype=\"" + file.getFileType() + "\"")
                    .body(file.getData());

        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<Task> reduceQuota(Integer id) {
        Task taskObj = taskRepo.getReferenceById(id);
        taskObj.setQuota(taskObj.getQuota()-1);
        taskRepo.save(taskObj);
        return ResponseEntity.ok(taskObj);
    }

    public ResponseEntity<Task> addQuota(Integer id){
        Task taskObj = taskRepo.getReferenceById(id);
        taskObj.setQuota(taskObj.getQuota()+1);
        taskRepo.save(taskObj);
        return ResponseEntity.ok(taskObj);
    }


    @GetMapping("/availabletasks")
    public ResponseEntity<List<Task>> getAllAvailableTasks() {
        try {
            List<Task> tasks = new ArrayList<>(taskRepo.findAll());
            if (tasks.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            List<Task> AvailableTasks = new ArrayList<Task>();
            for (Task task : tasks){
                if (task.getQuota()>0){
                    AvailableTasks.add(task);
                }
            }
            return ResponseEntity.ok(AvailableTasks);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @GetMapping("/task/project/{id}")
    public ResponseEntity<List<Task>> getTaskFromProjectId(@PathVariable Integer id) {
        try {
            List<Task> tasks = new ArrayList<>(taskRepo.findAll());
            if (tasks.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            List<Task> AvailableTasks = new ArrayList<Task>();
            for (Task task : tasks){
                if (task.getProjectId()==id){
                    AvailableTasks.add(task);
                }
            }
            return ResponseEntity.ok(AvailableTasks);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @PostMapping("/task")
    public ResponseEntity<Task> addTask(@RequestBody Task task){
        try {
            Task taskObj = taskRepo.save(task);
            projectController.addTask(task.getProjectId(), task.getId());
            return new ResponseEntity<>(taskObj, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/task/{id}")
    public ResponseEntity<Task> deleteTask(@PathVariable Integer id) {
        try {
            taskRepo.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/testcase")
    public ResponseEntity<TestCase> addTestcase(@RequestBody TestCase testCase){
        try{
            testCaseRepo.save(testCase);

            Task task = taskRepo.getReferenceById(testCase.getTaskId());
            List<Integer> testCaseList = task.getTestCaseIds();
            testCaseList.add(testCase.getId());
            task.setTestCaseIds(testCaseList);
            taskRepo.save(task);
            
            return new ResponseEntity<>(testCase, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/task/{id}/testcase")
    public ResponseEntity<List<TestCase>> getTestCase(@PathVariable Integer id) {
        try {
            List<TestCase> testCases = new ArrayList<>(testCaseRepo.findAll());
            List<TestCase> taskCases = new ArrayList<>();
            for (TestCase testCase : testCases){
                if (testCase.getTaskId().equals(id)){
                    taskCases.add(testCase);
                }
            }
            if (taskCases.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return ResponseEntity.ok(taskCases);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
