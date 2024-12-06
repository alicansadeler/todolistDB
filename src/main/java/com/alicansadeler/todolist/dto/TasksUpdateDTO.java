package com.alicansadeler.todolist.dto;

import com.alicansadeler.todolist.enums.Priority;
import com.alicansadeler.todolist.enums.Status;

import java.util.Date;

public record TasksUpdateDTO(
        String title,
        String description,
        Status status,
        Priority priority,
        Date dueDate
) {
}
