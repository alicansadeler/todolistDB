package com.alicansadeler.todolist.controller;

import com.alicansadeler.todolist.dto.BaseResponseDTO;
import com.alicansadeler.todolist.dto.TasksRequestDTO;
import com.alicansadeler.todolist.dto.TasksResponseDTO;
import com.alicansadeler.todolist.dto.TasksUpdateDTO;
import com.alicansadeler.todolist.enums.Status;
import com.alicansadeler.todolist.paths.ApiUrls;
import com.alicansadeler.todolist.service.TasksService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
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

    @Operation(summary = "Yeni görev oluştur")
    @ApiResponse(responseCode = "201", description = "Görev oluşturuldu")
    @ApiResponse(responseCode = "404", description = "Kullanıcı bulunamadı")
    @PostMapping
    public ResponseEntity<BaseResponseDTO<TasksResponseDTO>> post(@Valid @RequestBody TasksRequestDTO taskResponseDTO) {
        BaseResponseDTO<TasksResponseDTO> tasksServiceTask = tasksService.createTask(taskResponseDTO);
        return ResponseEntity.status(tasksServiceTask.isSuccess() ? HttpStatus.CREATED : HttpStatus.BAD_REQUEST)
                .body(tasksServiceTask);
    }

    @Operation(summary = "Görevi ID ile getir")
    @ApiResponse(responseCode = "200", description = "Görev bulundu")
    @ApiResponse(responseCode = "404", description = "Görev bulunamadı")
    @GetMapping(ApiUrls.ID)
    public ResponseEntity<BaseResponseDTO<TasksResponseDTO>> getTask(@PathVariable int id) {
        BaseResponseDTO<TasksResponseDTO> tasksServiceTask = tasksService.getTaskById(id);
        return ResponseEntity.status(tasksServiceTask.isSuccess() ? HttpStatus.OK : HttpStatus.NOT_FOUND)
                .body(tasksServiceTask);
    }

    @Operation(summary = "Tüm görevleri listele")
    @ApiResponse(responseCode = "200", description = "Görevler listelendi")
    @GetMapping
    public ResponseEntity<BaseResponseDTO<List<TasksResponseDTO>>> getAllTasks() {
        return ResponseEntity.ok(tasksService.getAllTasks());
    }

    @Operation(summary = "Kullanıcının görevlerini getir")
    @ApiResponse(responseCode = "200", description = "Kullanıcının görevleri listelendi")
    @ApiResponse(responseCode = "404", description = "Kullanıcı bulunamadı")
    @GetMapping(ApiUrls.BY_USER_ID)
    public ResponseEntity<BaseResponseDTO<List<TasksResponseDTO>>> getTasksByUserId(@PathVariable int userId) {
        BaseResponseDTO<List<TasksResponseDTO>> tasksServiceTask = tasksService.getTasksByUserId(userId);
        return ResponseEntity.status(tasksServiceTask.isSuccess() ? HttpStatus.OK : HttpStatus.NOT_FOUND)
                .body(tasksServiceTask);
    }

    @Operation(summary = "Duruma göre görevleri getir")
    @ApiResponse(responseCode = "200", description = "Görevler listelendi")
    @GetMapping(ApiUrls.BY_STATUS)
    public ResponseEntity<BaseResponseDTO<List<TasksResponseDTO>>> getTasksByStatus(@PathVariable Status status) {
        return ResponseEntity.ok(tasksService.getTasksByStatus(status));
    }

    @Operation(summary = "Görevi güncelle")
    @ApiResponse(responseCode = "200", description = "Görev güncellendi")
    @ApiResponse(responseCode = "404", description = "Görev bulunamadı")
    @PutMapping(ApiUrls.ID)
    public ResponseEntity<BaseResponseDTO<TasksResponseDTO>> updateTask(
            @PathVariable int id,
            @Valid @RequestBody TasksUpdateDTO tasksUpdateDTO) {
        BaseResponseDTO<TasksResponseDTO> tasksServiceTask = tasksService.updateTask(id, tasksUpdateDTO);
        return ResponseEntity.status(tasksServiceTask.isSuccess() ? HttpStatus.OK : HttpStatus.NOT_FOUND)
                .body(tasksServiceTask);
    }

    @Operation(summary = "Görevi sil")
    @ApiResponse(responseCode = "200", description = "Görev silindi")
    @ApiResponse(responseCode = "404", description = "Görev bulunamadı")
    @DeleteMapping(ApiUrls.ID)
    public ResponseEntity<BaseResponseDTO<String>> deleteTask(@PathVariable int id) {
        BaseResponseDTO<String> tasksServiceTask = tasksService.deleteTask(id);
        return ResponseEntity.status(tasksServiceTask.isSuccess() ? HttpStatus.OK : HttpStatus.NOT_FOUND)
                .body(tasksServiceTask);
    }
}
