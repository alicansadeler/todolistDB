package com.alicansadeler.todolist.service;

import com.alicansadeler.todolist.dto.BaseResponseDTO;
import com.alicansadeler.todolist.dto.TasksRequestDTO;
import com.alicansadeler.todolist.dto.TasksResponseDTO;
import com.alicansadeler.todolist.entity.Tasks;
import com.alicansadeler.todolist.entity.User;
import com.alicansadeler.todolist.enums.Priority;
import com.alicansadeler.todolist.enums.Status;
import com.alicansadeler.todolist.exceptions.ApiException;
import com.alicansadeler.todolist.mapper.TasksMapper;
import com.alicansadeler.todolist.repository.TasksRepository;
import com.alicansadeler.todolist.repository.UserRepository;
import com.alicansadeler.todolist.service.impl.TasksServiceImpl;
import jakarta.validation.constraints.AssertTrue;
import net.bytebuddy.build.ToStringPlugin;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TaskServiceTest {
    @Mock
    private TasksRepository tasksRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private TasksMapper tasksMapper;

    @InjectMocks
    private TasksServiceImpl tasksService;

    private List<Tasks> mockTasks;
    private List<TasksResponseDTO> mockTaskResponses;
    private User mockUser;

    @BeforeEach
    void setUp() {
        mockUser = User.builder()
                .id(1)
                .username("testUser")
                .build();

        mockTasks = List.of(
                Tasks.builder()
                        .id(1)
                        .title("Task 1")
                        .description("Desc 1")
                        .status(Status.TODO)
                        .priority(Priority.LOW)
                        .user(mockUser)
                        .build(),
                Tasks.builder()
                        .id(2)
                        .title("Task 2")
                        .description("Desc 2")
                        .status(Status.IN_PROGRESS)
                        .priority(Priority.HIGH)
                        .user(mockUser)
                        .build()
        );

        mockTaskResponses = mockTasks.stream()
                .map(task -> new TasksResponseDTO(
                        task.getId(),
                        task.getTitle(),
                        task.getDescription(),
                        task.getStatus(),
                        task.getPriority(),
                        null, null, null,
                        mockUser.getId()))
                .toList();
    }

    @Test
    void createTask_Success() {
        TasksRequestDTO request = new TasksRequestDTO(
                "Task 1",
                "Desc 1",
                Status.TODO,
                Priority.LOW,
                null,
                mockUser.getId()
        );

        when(userRepository.findById(mockUser.getId())).thenReturn(Optional.of(mockUser));
        when(tasksRepository.save(any())).thenReturn(mockTasks.get(0));
        when(tasksMapper.toDTO(any())).thenReturn(mockTaskResponses.get(0));

        BaseResponseDTO<TasksResponseDTO> response = tasksService.createTask(request);

        assertNotNull(response);
        assertTrue(response.isSuccess());
        assertEquals(mockTaskResponses.get(0), response.getData());
        verify(userRepository).findById(mockUser.getId());
        verify(tasksRepository).save(any());
        verify(tasksMapper).toDTO(any());
    }

    @Test
    void createTask_UnSuccess() {
        int existUserId = 999;
        TasksRequestDTO request = new TasksRequestDTO("testTitle", "testDescription",
                Status.TODO, Priority.MEDIUM, null, existUserId);

        when(userRepository.findById(existUserId)).thenReturn(Optional.empty());

        assertThrows(ApiException.class, () -> tasksService.createTask(request));
        verify(userRepository, times(1)).findById(existUserId);
        verify(tasksRepository, never()).save(any());
    }

    @Test
    void getTaskById_Success() {
        int taskId = mockTasks.get(0).getId();

        when(tasksRepository.findById(taskId)).thenReturn(Optional.of(mockTasks.get(0)));
        when(tasksMapper.toDTO(mockTasks.get(0))).thenReturn(mockTaskResponses.get(0));

        BaseResponseDTO<TasksResponseDTO> response = tasksService.getTaskById(taskId);

        assertNotNull(response);
        assertTrue(response.isSuccess());
        assertEquals(mockTaskResponses.get(0), response.getData());
        verify(tasksRepository).findById(taskId);
        verify(tasksMapper).toDTO(any());
    }

    @Test
    void getTaskById_UnSuccess() {
        int taskId = 999;


        when(tasksRepository.findById(taskId)).thenReturn(Optional.empty());

        assertThrows(ApiException.class, () -> tasksService.getTaskById(taskId));

        verify(tasksRepository).findById(any());
        verify(tasksMapper, never()).toDTO(any());
    }

    @Test
    void getAllTasks_Success() {

        when(tasksRepository.findAll()).thenReturn(mockTasks);
        when(tasksMapper.toDTO(any())).thenReturn(mockTaskResponses.get(0), mockTaskResponses.get(1));

        BaseResponseDTO<List<TasksResponseDTO>> response = tasksService.getAllTasks();

        assertNotNull(response);
        assertTrue(response.isSuccess());
        assertEquals(mockTasks.size(), response.getData().size());
        verify(tasksRepository).findAll();
        verify(tasksMapper, times(mockTasks.size())).toDTO(any());
    }

    @Test
    void getTasksByUserId_Success() {
        when(userRepository.findById(mockUser.getId())).thenReturn(Optional.of(mockUser));
        when(tasksRepository.findByUser(mockUser)).thenReturn(mockTasks);
        when(tasksMapper.toDTO(any())).thenReturn(mockTaskResponses.get(0), mockTaskResponses.get(1));

        BaseResponseDTO<List<TasksResponseDTO>> response = tasksService.getTasksByUserId(mockUser.getId());


        assertNotNull(response);
        assertTrue(response.isSuccess());
        assertEquals(mockTasks.size(), response.getData().size());
        verify(userRepository).findById(mockUser.getId());
        verify(tasksRepository).findByUser(mockUser);
        verify(tasksMapper, times(mockTasks.size())).toDTO(any());
    }

    @Test
    void getTasksByUserId_UnSuccess() {
        int userId = 999;

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThrows(ApiException.class, () -> tasksService.getTasksByUserId(userId));

        verify(userRepository,times(1)).findById(userId);
        verify(tasksRepository, never()).findByUser(any());
    }

    @Test
    void getTasksByStatus_Success() {
        when(tasksRepository.findByStatus(any())).thenReturn(mockTasks);
        when(tasksMapper.toDTO(any())).thenReturn(mockTaskResponses.get(0), mockTaskResponses.get(1));

        BaseResponseDTO<List<TasksResponseDTO>> response = tasksService.getTasksByStatus(mockTasks.get(0).getStatus());


        assertNotNull(response);
        assertTrue(response.isSuccess());
        assertEquals(mockTasks.size(), response.getData().size());
        verify(tasksRepository).findByStatus(mockTasks.get(0).getStatus());
        verify(tasksMapper, times(mockTasks.size())).toDTO(any());

    }
}
