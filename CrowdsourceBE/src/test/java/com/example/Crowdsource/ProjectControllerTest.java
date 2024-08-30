package com.example.Crowdsource;


import com.example.Crowdsource.controller.ProjectController;
import com.example.Crowdsource.model.Project;
import com.example.Crowdsource.model.Task;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(ProjectController.class)
public class ProjectControllerTest {
    @Autowired
    private MockMvc mvc;

    @MockBean
    private ProjectController projectController;

    @Test
    public void getProjectDetail() throws Exception {
        given(projectController.getProjectByID(1)).willReturn(ResponseEntity.ok(mock()));

        mvc.perform(get("/projects/1"))
                .andExpect(status().isOk());
    }

    @Test
    public void getProjectsFromUser() throws Exception {
        List<Project> projects = List.of(
                new Project()
        );
        given(projectController.getProjectByRequesterId(1)).willReturn(ResponseEntity.ok(projects));

        mvc.perform(get("/projects/user/1"))
                .andExpect(status().isOk());
    }

    @Test
    public void createProject() throws Exception {
        given(projectController.createProject(any(Project.class))).willReturn(ResponseEntity.ok(mock()));

        mvc.perform(post("/projects")
                .content("{\"name\":\"New Project\",\"description\":\"Project Description\"}")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}
