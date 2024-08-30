package com.example.Crowdsource.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name="File")
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class File {
    @Id
    private Integer id;
    private String fileType;
    @Lob
    private byte[] data;

    // Getters and setters
}

