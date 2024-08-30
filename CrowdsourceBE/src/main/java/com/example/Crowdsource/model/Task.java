package com.example.Crowdsource.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.*;

import java.sql.Time;
import java.sql.Timestamp;
import java.time.Duration;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;

@Entity
@Table(name="Task")
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@ToString
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    private String name;
    private String description;
    private String exe;

    private Integer projectId;

    @ElementCollection
    private List<Integer> AssignmentIds = new ArrayList<>();;

    @ElementCollection
    private List<Integer> TestCaseIds = new ArrayList<>();

    private String updateTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date deadline;
    private Integer quota;
}
