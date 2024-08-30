package com.example.Crowdsource.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name="TestCAse")
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class TestCase {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    private Integer taskId;
    private String name;
    private String instruction;
    private String input;
    private String output;
    // Getters and setters
}
