package com.alicansadeler.todolist.service.impl;

import com.alicansadeler.todolist.dto.BaseResponseDTO;
import com.alicansadeler.todolist.dto.TasksRequestDTO;
import com.alicansadeler.todolist.dto.TasksResponseDTO;
import com.alicansadeler.todolist.dto.TasksUpdateDTO;
import com.alicansadeler.todolist.entity.Tasks;
import com.alicansadeler.todolist.entity.User;
import com.alicansadeler.todolist.enums.Status;
import com.alicansadeler.todolist.exceptions.ApiException;
import com.alicansadeler.todolist.mapper.TasksMapper;
import com.alicansadeler.todolist.repository.TasksRepository;
import com.alicansadeler.todolist.repository.UserRepository;
import com.alicansadeler.todolist.service.TasksService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class TasksServiceImpl implements TasksService {

    private final TasksRepository tasksRepository;
    private final UserRepository userRepository;
    private final TasksMapper taskMapper;

    @Override
    public BaseResponseDTO<TasksResponseDTO> createTask(TasksRequestDTO taskRequest) {
        log.info("Creating new task for user id: {}", taskRequest.userId());
        User user = userRepository.findById(taskRequest.userId())
                .orElseThrow(() -> new ApiException("User not found with id: " + taskRequest.userId(), HttpStatus.NOT_FOUND));

        Tasks task = taskMapper.toEntity(taskRequest, user);
        Tasks savedTask = tasksRepository.save(task);
        log.info("Task created successfully with id: {}", savedTask.getId());
        return new BaseResponseDTO<>(true, "Task created successfully", taskMapper.toDTO(savedTask));
    }

    @Override
    public BaseResponseDTO<TasksResponseDTO> getTaskById(int id) {
        log.info("Fetching task with id: {}", id);
        Tasks task = tasksRepository.findById(id)
                .orElseThrow(() -> new ApiException("Task not found with id: " + id, HttpStatus.NOT_FOUND));

        log.info("Task found with id: {}", id);
        return new BaseResponseDTO<>(true, "Task found", taskMapper.toDTO(task));
    }

    @Override
    public BaseResponseDTO<List<TasksResponseDTO>> getAllTasks() {
        log.info("Fetching all tasks");
        List<Tasks> tasks = tasksRepository.findAll();
        List<TasksResponseDTO> taskDTO = tasks.stream()
                .map(taskMapper::toDTO)
                .toList();
        log.info("Found {} tasks", taskDTO.size());
        return new BaseResponseDTO<>(true, "Tasks listed successfully", taskDTO);
    }

    @Override
    public BaseResponseDTO<List<TasksResponseDTO>> getTasksByUserId(int userId) {
        log.info("Fetching tasks for user id: {}", userId);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ApiException("User not found with id: " + userId, HttpStatus.NOT_FOUND));

        List<Tasks> tasks = tasksRepository.findByUser(user);
        List<TasksResponseDTO> taskDTOs = tasks.stream()
                .map(taskMapper::toDTO)
                .toList();
        log.info("Found {} tasks for user id: {}", taskDTOs.size(), userId);
        return new BaseResponseDTO<>(true, "Tasks found", taskDTOs);
    }

    @Override
    public BaseResponseDTO<List<TasksResponseDTO>> getTasksByStatus(Status status) {
        log.info("Fetching tasks with status: {}", status);
        List<Tasks> tasks = tasksRepository.findByStatus(status);
        List<TasksResponseDTO> taskDTOs = tasks.stream()
                .map(taskMapper::toDTO)
                .toList();
        log.info("Found {} tasks with status {}", taskDTOs.size(), status);
        return new BaseResponseDTO<>(true, "Tasks found", taskDTOs);
    }

    @Override
    public BaseResponseDTO<TasksResponseDTO> updateTask(int id, TasksUpdateDTO request) {
        log.info("Attempting to update task with id: {}", id);
        Tasks task = tasksRepository.findById(id)
                .orElseThrow(() -> new ApiException("Task not found with id: " + id, HttpStatus.NOT_FOUND));

        task.setTitle(request.title());
        task.setDescription(request.description());
        task.setStatus(request.status());
        task.setPriority(request.priority());
        task.setDueDate(request.dueDate());

        Tasks updatedTask = tasksRepository.save(task);
        log.info("Task updated successfully with id: {}", id);
        return new BaseResponseDTO<>(true, "Task updated successfully", taskMapper.toDTO(updatedTask));
    }

    @Override
    public BaseResponseDTO<String> deleteTask(int id) {
        log.info("Attempting to delete task with id: {}", id);
        if (!tasksRepository.existsById(id)) {
            throw new ApiException("Task not found with id: " + id, HttpStatus.NOT_FOUND);
        }

        tasksRepository.deleteById(id);
        log.info("Task deleted successfully with id: {}", id);
        return new BaseResponseDTO<>(true, "Task deleted successfully", null);
    }
}