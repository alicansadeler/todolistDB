package com.alicansadeler.todolist.mapper;

import com.alicansadeler.todolist.dto.TasksRequestDTO;
import com.alicansadeler.todolist.dto.TasksResponseDTO;
import com.alicansadeler.todolist.entity.Tasks;
import com.alicansadeler.todolist.entity.User;
import org.springframework.stereotype.Component;

@Component
public class TasksMapper {
    public TasksResponseDTO toDTO(Tasks task) {
        return new TasksResponseDTO(
                task.getId(),
                task.getTitle(),
                task.getDescription(),
                task.getStatus(),
                task.getPriority(),
                task.getDueDate(),
                task.getCreatedAt(),
                task.getUpdatedAt(),
                task.getUser().getId()
        );
    }

    public Tasks toEntity(TasksRequestDTO dto, User user) {
        Tasks task = new Tasks();
        task.setTitle(dto.title());
        task.setDescription(dto.description());
        task.setStatus(dto.status());
        task.setPriority(dto.priority());
        task.setDueDate(dto.dueDate());
        task.setUser(user);
        return task;
    }
}
