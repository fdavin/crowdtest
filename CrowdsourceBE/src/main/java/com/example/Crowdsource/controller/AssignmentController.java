package com.example.Crowdsource.controller;

import com.example.Crowdsource.model.Assignment;
import com.example.Crowdsource.model.Task;
import com.example.Crowdsource.repo.AssignmentRepo;
import com.example.Crowdsource.repo.TaskRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.*;

import static java.lang.Integer.parseInt;

@RestController
public class AssignmentController {
    @Autowired
    private AssignmentRepo assignmentRepo;
    @Autowired
    TaskController taskController;
    @Autowired
    private TaskRepo taskRepo;

    @GetMapping("/assignment/{id}")
    public ResponseEntity<Assignment> getAssignment(@PathVariable Integer id) {
        try {
            Optional<Assignment> assignment = assignmentRepo.findById(id);
            return assignment.map(value -> new ResponseEntity<>(value, HttpStatus.OK)).orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
        }catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @GetMapping("/activeAssignment/{id}")
    public ResponseEntity<List<Assignment>> getAllAssignments(@PathVariable Integer id) {
        try {
            List<Assignment> assignments = new ArrayList<>(assignmentRepo.findAll());
            List<Assignment> finalAssignments = new ArrayList<>();
            for (Assignment assignment : assignments){
                if (Objects.equals(assignment.getAssigneeId(), id)){
                    finalAssignments.add(assignment);
                }
            }
            if (finalAssignments.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return ResponseEntity.ok(finalAssignments);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/assignment")
    public ResponseEntity<Assignment> addAssignment(@RequestBody Assignment assignment){
        try {
            Assignment assignmentObj = new Assignment();
            assignmentObj.setAssigneeId(assignment.getAssigneeId());
            assignmentObj.setTaskId(assignment.getTaskId());
            Task taskObj = taskRepo.getReferenceById(assignment.getTaskId());
            Date deadline = new Date();
            Calendar cal = Calendar.getInstance();
            cal.setTime(deadline);
            cal.add(Calendar.DATE, parseInt(taskObj.getUpdateTime()));
            deadline.setTime(cal.getTime().getTime());
            if (deadline.after(taskObj.getDeadline())){
                deadline.setTime(taskObj.getDeadline().getTime());
            }
            assignmentObj.setUpdateDeadline(deadline);
            assignmentObj.setCurrentProgress((float) 0);
            assignmentObj.setProgressProof("");
            assignmentObj.setCompletionProof("");
            assignmentRepo.save(assignmentObj);
            taskController.reduceQuota(assignmentObj.getTaskId());
            return new ResponseEntity<>(assignmentObj, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/assignment/task/{id}")
    public ResponseEntity<List<Assignment>> getAssignmentFromTaskId(@PathVariable Integer id) {
        try {
            List<Assignment> assignments = assignmentRepo.findAll();
            List<Assignment> finalList = new ArrayList<>();
            for (Assignment assignment : assignments){
                if (Objects.equals(assignment.getTaskId(), id)){
                    finalList.add(assignment);
                }
            }
            return ResponseEntity.ok(finalList);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @PutMapping("/assignment/update/{id}")
    public ResponseEntity<Assignment> updateAssignment(@PathVariable Integer id, @RequestBody Assignment assignment){
        try{
            Assignment assignmentObj = assignmentRepo.getReferenceById(id);
            Task taskObj = taskRepo.getReferenceById(assignmentObj.getTaskId());
            assignmentObj.setCurrentProgress(assignment.getCurrentProgress());
            assignmentObj.setProgressProof(assignment.getProgressProof());
            assignmentObj.setCompletionProof(assignment.getCompletionProof());
            Date newTime = new Date();
            Calendar cal = Calendar.getInstance();
            cal.setTime(newTime);
            cal.add(Calendar.DATE, parseInt(taskObj.getUpdateTime()));
            newTime.setTime(cal.getTime().getTime());
            if (newTime.after(taskObj.getDeadline())){
                newTime.setTime(taskObj.getDeadline().getTime());
            }
            assignmentObj.setUpdateDeadline(newTime);
            assignmentRepo.save(assignmentObj);
            return ResponseEntity.ok(assignmentObj);
        } catch (Exception e){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @DeleteMapping("/assignment/delete/{id}")
    public ResponseEntity<Assignment> deleteAssignment(@PathVariable Integer id) {
        try {
            Assignment assignmentObj = assignmentRepo.getReferenceById(id);
            Task taskObj = taskRepo.getReferenceById(assignmentObj.getTaskId());
            taskController.addQuota(taskObj.getId());
            assignmentRepo.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/assignment/finish/{id}")
    public ResponseEntity<Assignment> finishAssignment(@PathVariable Integer id) {
        try {
            assignmentRepo.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


}
