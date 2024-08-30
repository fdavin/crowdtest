package com.example.Crowdsource;

import com.example.Crowdsource.controller.UserController;
import com.example.Crowdsource.model.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(UserController.class)
public class UserControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private UserController userController;

    @Test
    public void getUser() throws Exception {

        given(userController.getUserByID(1)).willReturn(ResponseEntity.ok(mock()));

        mvc.perform(get("/users/1"))
                .andExpect(status().isOk());
    }

    @Test
    public void login() throws Exception {
        User user = new User(1,"name","email","password","Worker");

        given(userController.login(user)).willReturn(ResponseEntity.ok(user));

        mvc.perform(post("/login")
                .content("{\"id\":1,\"name\":\"name\",\"email\":\"email\",\"password\":\"password\",\"role\":\"Worker\"}")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void register() throws Exception {
        User user = new User(1,"name","email","password","Worker");

        given(userController.createUser(user)).willReturn(ResponseEntity.ok(user));

        mvc.perform(post("/users")
                .content("{\"id\":1,\"name\":\"name\",\"email\":\"email\",\"password\":\"password\",\"role\":\"Worker\"}")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}
