package com.example.Crowdsource;


import com.example.Crowdsource.controller.ProjectController;
import com.example.Crowdsource.controller.TaskController;
import com.example.Crowdsource.model.Task;
import com.example.Crowdsource.model.TestCase;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(TaskController.class)
public class TaskControllerTest {
    @Autowired
    private MockMvc mvc;

    @MockBean
    private TaskController taskController;

    @MockBean
    private ProjectController projectController;

    @Test
    public void getTaskDetail() throws Exception {
        given(taskController.getTask(1)).willReturn(ResponseEntity.ok(mock()));

        mvc.perform(get("/task/1"))
                .andExpect(status().isOk());
    }

    @Test
    public void getTasksFromProject() throws Exception {
        List<Task> tasks = List.of(
                new Task()
        );

        given(taskController.getTaskFromProjectId(1)).willReturn(ResponseEntity.ok(tasks));

        mvc.perform(get("/task/project/1"))
                .andExpect(status().isOk());
    }

    @Test
    public void createTask() throws Exception {
        given(taskController.addTask(any(Task.class))).willReturn(ResponseEntity.ok(mock()));
        given(projectController.addTask(any(Integer.class),any(Integer.class))).willReturn(ResponseEntity.ok(mock()));

        mvc.perform(post("/task")
                .content("{\"id\":1,\"name\":\"New Task\",\"description\":\"Task Description\"}")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void getAvailableTasks() throws Exception {
        List<Task> tasks = List.of(
                new Task()
        );

        given(taskController.getAllAvailableTasks()).willReturn(ResponseEntity.ok(tasks));

        mvc.perform(get("/availabletasks"))
                .andExpect(status().isOk());
    }

    @Test
    public void createTestCase() throws Exception {
        given(taskController.addTestcase(any(TestCase.class))).willReturn(ResponseEntity.ok(mock()));

        mvc.perform(post("/testcase")
                        .content("{\"id\":1,\"name\":\"New Test Case\",\"description\":\"Test Case Description\"}")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void getTestCaseFromTask() throws Exception {
        List<TestCase> testCases = List.of(
                new TestCase()
        );

        given(taskController.getTestCase(1)).willReturn(ResponseEntity.ok(testCases));

        mvc.perform(get("/task/1/testcase"))
                .andExpect(status().isOk());
    }

//    @Test
//    public void uploadExecutable() throws Exception{
//
//        given(taskController.uploadFile(any(MultipartFile.class), eq(1)))
//                .willReturn(ResponseEntity.ok(mock()));
//
//        mvc.perform(MockMvcRequestBuilders.multipart("/task/upload/1")
//                .file(mock(MockMultipartFile.class))
//                .contentType(MediaType.MULTIPART_FORM_DATA))
//                .andExpect(status().isOk());
//    }
//
//    @Test
//    public void downloadExecutable() throws Exception {
//        given(taskController.downloadFile(1)).willReturn(ResponseEntity.ok(mock()));
//
//        mvc.perform(get("task/download/1"))
//                .andExpect(status().isOk());
//    }
}
