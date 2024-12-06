package com.alicansadeler.todolist.service;

import com.alicansadeler.todolist.dto.BaseResponseDTO;
import com.alicansadeler.todolist.dto.TasksRequestDTO;
import com.alicansadeler.todolist.dto.TasksResponseDTO;
import com.alicansadeler.todolist.dto.TasksUpdateDTO;
import com.alicansadeler.todolist.enums.Status;

import java.util.List;

public interface TasksService {
    BaseResponseDTO<TasksResponseDTO> createTask(TasksRequestDTO request);
    BaseResponseDTO<TasksResponseDTO> getTaskById(int id);
    BaseResponseDTO<List<TasksResponseDTO>> getAllTasks();
    BaseResponseDTO<List<TasksResponseDTO>> getTasksByUserId(int userId);
    BaseResponseDTO<List<TasksResponseDTO>> getTasksByStatus(Status status);
    BaseResponseDTO<TasksResponseDTO> updateTask(int id, TasksUpdateDTO request);
    BaseResponseDTO<String> deleteTask(int id);
}
