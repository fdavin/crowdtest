package com.example.Crowdsource;


import com.example.Crowdsource.controller.AssignmentController;
import com.example.Crowdsource.controller.TaskController;
import com.example.Crowdsource.model.Assignment;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(AssignmentController.class)
public class AssignmentControllerTest {
    @Autowired
    private MockMvc mvc;

    @MockBean
    private AssignmentController assignmentController;

    @MockBean
    private TaskController taskController;

    @Test
    public void getAssignmentDetail() throws Exception {
        given(assignmentController.getAssignment(1)).willReturn(ResponseEntity.ok(mock()));

        mvc.perform(get("/assignment/1"))
                .andExpect(status().isOk());
    }

    @Test
    public void createAssignment() throws Exception {
        given(assignmentController.addAssignment(any(Assignment.class))).willReturn(ResponseEntity.ok(mock()));
        given(taskController.reduceQuota(1)).willReturn(ResponseEntity.ok(mock()));

        mvc.perform(post("/assignment")
                .content("{\"id\":1,\"name\":\"New Assignment\"}")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void getAssignmentofTask() throws Exception {
        List<Assignment> assignments = List.of(
                new Assignment()
        );

        given(assignmentController.getAssignmentFromTaskId(1)).willReturn(ResponseEntity.ok(assignments));

        mvc.perform(get("/assignment/task/1"))
                .andExpect(status().isOk());
    }

    @Test
    public void getUsersAssignment() throws Exception {
        List<Assignment> assignments = List.of(
                new Assignment()
        );

        given(assignmentController.getAllAssignments(1)).willReturn(ResponseEntity.ok(assignments));

        mvc.perform(get("/activeAssignment/1"))
                .andExpect(status().isOk());
    }

    @Test
    public void updateAssignment() throws Exception {
        given(assignmentController.updateAssignment(eq(1), any(Assignment.class))).willReturn(ResponseEntity.ok(mock()));
        mvc.perform(put("/assignment/update/1")
                        .content("{\"ProgressProof\":1,\"CompletionProof\":\"1\"}")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());


    }

    @Test
    public void FinishAssignment() throws Exception{
        given(assignmentController.finishAssignment(1)).willReturn(new ResponseEntity<>(HttpStatus.NO_CONTENT));
        mvc.perform(delete("/assignment/finish/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    public void CancelAssignment() throws Exception{
        given(assignmentController.deleteAssignment(1)).willReturn(new ResponseEntity<>(HttpStatus.NO_CONTENT));
        given(taskController.addQuota(1)).willReturn(ResponseEntity.ok(mock()));
        mvc.perform(delete("/assignment/delete/1"))
                .andExpect(status().isNoContent());
    }
}
