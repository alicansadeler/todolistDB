package com.alicansadeler.todolist.service;

import com.alicansadeler.todolist.dto.BaseResponseDTO;
import com.alicansadeler.todolist.dto.UserRequestDTO;
import com.alicansadeler.todolist.dto.UserResponseDTO;
import com.alicansadeler.todolist.entity.User;
import com.alicansadeler.todolist.exceptions.ApiException;
import com.alicansadeler.todolist.mapper.UserMapper;
import com.alicansadeler.todolist.repository.UserRepository;
import com.alicansadeler.todolist.service.impl.UserServiceImpl;
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
class UserServiceTest {
    @Mock
    private UserRepository userRepository;


    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private UserServiceImpl userService;



    @Test
    void getUserById_Successful() {
        int id = 1;
        User user = new User();
        user.setId(id);
        user.setUsername("testUser");

        UserResponseDTO userResponseDTO = new UserResponseDTO(user.getId(), user.getUsername(), null  );

        when(userRepository.findById(id)).thenReturn(Optional.of(user));
        when(userMapper.toDTO(user)).thenReturn(userResponseDTO);

        BaseResponseDTO<UserResponseDTO> response = userService.getUserById(id);

        assertNotNull(response);
        assertEquals(userResponseDTO, response.getData());
        verify(userRepository).findById(id);
        verify(userMapper).toDTO(user);
    }

    @Test
    void getUserById_unsuccessful() {
        int userId = 1;
        when(userRepository.findById(any())).thenReturn(Optional.empty());
        assertThrows(ApiException.class, () -> userService.getUserById(userId));
        verify(userRepository).findById(userId);
    }


    @Test
    void getAllUser_Success() {
        User user1 = User.builder()
                .username("testUser1")
                .id(1)
                .createdAt(null)
                .build();
        User user2 = User.builder()
                .username("testUser2")
                .id(2)
                .createdAt(null)
                .build();
        List<User> users = List.of(user1, user2);
        when(userRepository.findAll()).thenReturn(users);
        when(userMapper.toDTO(any())).thenReturn(new UserResponseDTO(1, "testUser1", null));


        BaseResponseDTO<List<UserResponseDTO>> response = userService.getAllUsers();
        assertNotNull(response);
        assertTrue(response.isSuccess());
        assertEquals(2, response.getData().size());
        verify(userRepository).findAll();
        verify(userMapper, times(2)).toDTO(any());
    }

    @Test
    void deleteUser_Success() {
        User testUser = User.builder()
                .username("testUser").id(1).createdAt(null).build();

        when(userRepository.existsById(testUser.getId())).thenReturn(true);

        BaseResponseDTO<String> response = userService.deleteUser(testUser.getId());
        assertNotNull(response);
        assertTrue(response.isSuccess());
        verify(userRepository, times(1)).existsById(testUser.getId());
        verify(userRepository, times(1)).deleteById(testUser.getId());
    }

    @Test
    void deleteUser_UnSuccess() {

        int nonExistentUserId = 999;
        when(userRepository.existsById(nonExistentUserId)).thenReturn(false);


        assertThrows(ApiException.class, () -> userService.deleteUser(nonExistentUserId));

        verify(userRepository, times(1)).existsById(nonExistentUserId);
        verify(userRepository, never()).deleteById(any());
    }

    @Test
    void updateUser_Success() {

        int userId = 1;
        UserRequestDTO request = new UserRequestDTO("newUsername", "newPassword");

        User existingUser = User.builder()
                .id(userId)
                .username("oldUsername")
                .password("oldPassword")
                .build();

        User updatedUser = User.builder()
                .id(userId)
                .username(request.username())
                .password(request.password())
                .build();

        UserResponseDTO expectedResponse = new UserResponseDTO(userId, request.username(), null);


        when(userRepository.findById(userId)).thenReturn(Optional.of(existingUser));
        when(userRepository.save(any())).thenReturn(updatedUser);
        when(userMapper.toDTO(updatedUser)).thenReturn(expectedResponse);


        BaseResponseDTO<UserResponseDTO> response = userService.updateUser(userId, request);


        assertNotNull(response);
        assertTrue(response.isSuccess());
        assertEquals(expectedResponse, response.getData());


        verify(userRepository).findById(userId);
        verify(userRepository).save(any());
        verify(userMapper).toDTO(any());
    }


    @Test
    void updateUser_UnSuccess() {
        int userId = 999;
        UserRequestDTO request = new UserRequestDTO("newUsername", "newPassword");

        when(userRepository.findById(userId)).thenReturn(Optional.empty());


        assertThrows(ApiException.class, () -> userService.updateUser(userId, request));

        verify(userRepository).findById(userId);
        verify(userRepository, never()).save(any());
    }
}
