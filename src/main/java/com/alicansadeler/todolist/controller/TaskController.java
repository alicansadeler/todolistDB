package com.alicansadeler.todolist.controller;

import com.alicansadeler.todolist.dto.BaseResponseDTO;
import com.alicansadeler.todolist.dto.TasksRequestDTO;
import com.alicansadeler.todolist.dto.TasksResponseDTO;
import com.alicansadeler.todolist.dto.TasksUpdateDTO;
import com.alicansadeler.todolist.enums.Status;
import com.alicansadeler.todolist.paths.ApiUrls;
import com.alicansadeler.todolist.service.TasksService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(ApiUrls.TASKS)
public class TaskController {
    private final TasksService tasksService;

    @PostMapping
    public ResponseEntity<BaseResponseDTO<TasksResponseDTO>> post(@Valid @RequestBody TasksRequestDTO taskResponseDTO) {
        BaseResponseDTO<TasksResponseDTO> tasksServiceTask = tasksService.createTask(taskResponseDTO);
        return ResponseEntity.status(tasksServiceTask.isSuccess() ? HttpStatus.CREATED : HttpStatus.BAD_REQUEST)
                .body(tasksServiceTask);
    }

    @GetMapping(ApiUrls.ID)
    public ResponseEntity<BaseResponseDTO<TasksResponseDTO>> getTask(@PathVariable int id) {
        BaseResponseDTO<TasksResponseDTO> tasksServiceTask = tasksService.getTaskById(id);
        return ResponseEntity.status(tasksServiceTask.isSuccess() ? HttpStatus.OK : HttpStatus.NOT_FOUND)
                .body(tasksServiceTask);
    }

    @GetMapping
    public ResponseEntity<BaseResponseDTO<List<TasksResponseDTO>>> getAllTasks() {
        return ResponseEntity.ok(tasksService.getAllTasks());
    }

    @GetMapping(ApiUrls.BY_USER_ID)
    public ResponseEntity<BaseResponseDTO<List<TasksResponseDTO>>> getTasksByUserId(@PathVariable int userId) {
        BaseResponseDTO<List<TasksResponseDTO>> tasksServiceTask = tasksService.getTasksByUserId(userId);
        return ResponseEntity.status(tasksServiceTask.isSuccess() ? HttpStatus.OK : HttpStatus.NOT_FOUND)
                .body(tasksServiceTask);
    }

    @GetMapping(ApiUrls.BY_STATUS)
    public ResponseEntity<BaseResponseDTO<List<TasksResponseDTO>>> getTasksByStatus(@PathVariable Status status) {
        return ResponseEntity.ok(tasksService.getTasksByStatus(status));
    }

    @PutMapping(ApiUrls.ID)
    public ResponseEntity<BaseResponseDTO<TasksResponseDTO>> updateTask(
            @PathVariable int id,
            @Valid @RequestBody TasksUpdateDTO tasksUpdateDTO) {
        BaseResponseDTO<TasksResponseDTO> tasksServiceTask = tasksService.updateTask(id, tasksUpdateDTO);
        return ResponseEntity.status(tasksServiceTask.isSuccess() ? HttpStatus.OK : HttpStatus.NOT_FOUND)
                .body(tasksServiceTask);
    }

    @DeleteMapping(ApiUrls.ID)
    public ResponseEntity<BaseResponseDTO<String>> deleteTask(@PathVariable int id) {
        BaseResponseDTO<String> tasksServiceTask = tasksService.deleteTask(id);
        return ResponseEntity.status(tasksServiceTask.isSuccess() ? HttpStatus.OK : HttpStatus.NOT_FOUND)
                .body(tasksServiceTask);
    }
}
