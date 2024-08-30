package com.example.Crowdsource.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name="WebsiteUser")
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@ToString
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    private String name;
    private String email;
    private String password;
    private String role;
}
