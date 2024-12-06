package com.alicansadeler.todolist.dto;

import java.sql.Timestamp;

public record UserResponseDTO(int id, String username, Timestamp createdAt) {
}
