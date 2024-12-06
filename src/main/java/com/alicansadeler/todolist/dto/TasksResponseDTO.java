package com.alicansadeler.todolist.dto;

import com.alicansadeler.todolist.enums.Priority;
import com.alicansadeler.todolist.enums.Status;

import java.sql.Timestamp;
import java.util.Date;

public record TasksResponseDTO(int id, String title, String description, Status status, Priority priority, Date dueDate, Timestamp createdAt, Timestamp updatedAt
, int userId) {
}
