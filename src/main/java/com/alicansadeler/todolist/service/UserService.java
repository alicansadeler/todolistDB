package com.alicansadeler.todolist.service;

import com.alicansadeler.todolist.dto.BaseResponseDTO;
import com.alicansadeler.todolist.dto.UserRequestDTO;
import com.alicansadeler.todolist.dto.UserResponseDTO;

import java.util.List;

public interface UserService {
    BaseResponseDTO<UserResponseDTO> createUser(UserRequestDTO request);
    BaseResponseDTO<UserResponseDTO> getUserById(int id);
    BaseResponseDTO<List<UserResponseDTO>> getAllUsers();
    BaseResponseDTO<String> deleteUser(int id);
    BaseResponseDTO<UserResponseDTO> updateUser(int id, UserRequestDTO request);
}
