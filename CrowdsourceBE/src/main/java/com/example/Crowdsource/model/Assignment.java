package com.example.Crowdsource.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.*;

import java.sql.Time;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name="Assignment")
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@ToString
public class Assignment {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    private Integer taskId;
    private Integer assigneeId;
    private String progressProof;
    private String completionProof;
    private Float currentProgress;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateDeadline;
}
