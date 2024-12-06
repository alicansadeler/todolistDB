package com.alicansadeler.todolist.service.impl;

import com.alicansadeler.todolist.dto.BaseResponseDTO;
import com.alicansadeler.todolist.dto.TasksRequestDTO;
import com.alicansadeler.todolist.dto.TasksResponseDTO;
import com.alicansadeler.todolist.dto.TasksUpdateDTO;
import com.alicansadeler.todolist.entity.Tasks;
import com.alicansadeler.todolist.entity.User;
import com.alicansadeler.todolist.enums.Status;
import com.alicansadeler.todolist.mapper.TasksMapper;
import com.alicansadeler.todolist.repository.TasksRepository;
import com.alicansadeler.todolist.repository.UserRepository;
import com.alicansadeler.todolist.service.TasksService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TasksServiceImpl implements TasksService {

    private final TasksRepository tasksRepository;
    private final UserRepository userRepository;
    private final TasksMapper taskMapper;

    @Override
    public BaseResponseDTO<TasksResponseDTO> createTask(TasksRequestDTO taskRequest) {
        Optional<User> optionalUser = userRepository.findById(taskRequest.userId());

        if (optionalUser.isPresent()) {
            return new BaseResponseDTO<>(false, "User not found", null);
        }

        Tasks task = taskMapper.toEntity(taskRequest, optionalUser.get());
        Tasks savedTask = tasksRepository.save(task);
        return new BaseResponseDTO<>(true, "Task created successfully", taskMapper.toDTO(savedTask));
    }

    @Override
    public BaseResponseDTO<TasksResponseDTO> getTaskById(int id) {
        Optional<Tasks> tasks = tasksRepository.findById(id);

        if (tasks.isPresent()) {
            return new BaseResponseDTO<>(false, "Task not found", null);
        }

        return new BaseResponseDTO<>(true, "Task found", taskMapper.toDTO(tasks.get()));
    }

    @Override
    public BaseResponseDTO<List<TasksResponseDTO>> getAllTasks() {
        List<Tasks> tasks = tasksRepository.findAll();
        List<TasksResponseDTO> taskDTO = tasks.stream()
                .map(item -> taskMapper.toDTO(item))
                .toList();

        return new BaseResponseDTO<>(true, "Tasks listed successfully", taskDTO);
    }

    @Override
    public BaseResponseDTO<List<TasksResponseDTO>> getTasksByUserId(int userId) {
        Optional<User> optionalUser = userRepository.findById(userId);

        if (optionalUser.isEmpty()) {
            return new BaseResponseDTO<>(false, "User not found", null);
        }

        List<Tasks> tasks = tasksRepository.findByUser(optionalUser.get());
        List<TasksResponseDTO> taskDTOs = tasks.stream()
                .map(taskMapper::toDTO)
                .toList();

        return new BaseResponseDTO<>(true, "Tasks found", taskDTOs);
    }

    @Override
    public BaseResponseDTO<List<TasksResponseDTO>> getTasksByStatus(Status status) {
        List<Tasks> tasks = tasksRepository.findByStatus(status);
        List<TasksResponseDTO> taskDTOs = tasks.stream()
                .map(taskMapper::toDTO)
                .toList();

        return new BaseResponseDTO<>(true, "Tasks found", taskDTOs);
    }

    @Override
    public BaseResponseDTO<TasksResponseDTO> updateTask(int id, TasksUpdateDTO request) {
        Optional<Tasks> optionalTask = tasksRepository.findById(id);

        if (optionalTask.isEmpty()) {
            return new BaseResponseDTO<>(false, "Task not found", null);
        }

        Tasks task = optionalTask.get();
        task.setTitle(request.title());
        task.setDescription(request.description());
        task.setStatus(request.status());
        task.setPriority(request.priority());
        task.setDueDate(request.dueDate());

        Tasks updatedTask = tasksRepository.save(task);
        return new BaseResponseDTO<>(true, "Task updated successfully", taskMapper.toDTO(updatedTask));
    }

    @Override
    public BaseResponseDTO<String> deleteTask(int id) {
        if (!tasksRepository.existsById(id)) {
            return new BaseResponseDTO<>(false, "Task not found", null);
        }

        tasksRepository.deleteById(id);
        return new BaseResponseDTO<>(true, "Task deleted successfully", null);
    }
}
