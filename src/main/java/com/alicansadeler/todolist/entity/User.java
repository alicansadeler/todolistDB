package com.alicansadeler.todolist.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(schema = "public", name = "todo_users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotBlank(message = "Username cannot be null and blank")
    @Size(max = 50, message = "Username cannot be more than 50 characters")
    private String username;

    @NotBlank(message = "Username cannot be null and blank")
    @Size(max = 255, message = "Password cannot be more than 255 characters")
    private String password;

    private Timestamp createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = Timestamp.valueOf(LocalDateTime.now());
    }

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private Set<Tasks> tasksSet = new HashSet<>();
}
