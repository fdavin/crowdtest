package com.example.Crowdsource.controller;

import com.example.Crowdsource.model.User;
import com.example.Crowdsource.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
public class UserController {

    @Autowired
    private UserRepo userRepo;

    @GetMapping("/users")
    public ResponseEntity<List<User>> getAllUser() {
        try {
            List<User> users = new ArrayList<>(userRepo.findAll());
            if (users.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return ResponseEntity.ok(users);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<User> getUserByID(@PathVariable Integer id) {
        try{
            Optional<User> user = userRepo.findById(id);
            return user.map(value -> new ResponseEntity<>(value, HttpStatus.OK)).orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/users")
    public ResponseEntity<User> createUser(@RequestBody User user) {
        try {
            User userObj = userRepo.save(user);
            return new ResponseEntity<>(userObj, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @PostMapping("/login")
    public ResponseEntity<User> login(@RequestBody User user) {
        try {
            User userObj = userRepo.findByEmailAndPassword(user.getEmail(), user.getPassword());
            return new ResponseEntity<>(userObj, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @PutMapping("/users/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Integer id, @RequestBody User user) {
        if (userRepo.findById(id).isPresent()) {
            User userObj = userRepo.findById(id).get();
            if (user.getEmail() != null) {
                userObj.setEmail(user.getEmail());
            }
            if (user.getPassword() != null) {
                userObj.setPassword(user.getPassword());
            }
            if (user.getName() != null) {
                userObj.setName(user.getName());
            }
            userRepo.save(userObj);
            return new ResponseEntity<>(userObj, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
