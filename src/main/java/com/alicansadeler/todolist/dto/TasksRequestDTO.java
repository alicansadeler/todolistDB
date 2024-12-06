package com.alicansadeler.todolist.dto;

import com.alicansadeler.todolist.enums.Priority;
import com.alicansadeler.todolist.enums.Status;

import java.util.Date;

public record TasksRequestDTO(String title,
                              String description,
                              Status status,
                              Priority priority,
                              Date dueDate,
                              int userId) {
}
