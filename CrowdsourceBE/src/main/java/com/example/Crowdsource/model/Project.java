package com.example.Crowdsource.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="Project")
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@ToString
public class Project {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    private String title;
    private String description;
    private Integer requesterId;

    @ElementCollection
    private List<Integer> taskIds = new ArrayList<>();;
}
