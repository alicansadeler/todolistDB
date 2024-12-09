package com.alicansadeler.todolist.service.impl;

import com.alicansadeler.todolist.dto.BaseResponseDTO;
import com.alicansadeler.todolist.dto.UserRequestDTO;
import com.alicansadeler.todolist.dto.UserResponseDTO;
import com.alicansadeler.todolist.entity.User;
import com.alicansadeler.todolist.exceptions.ApiException;
import com.alicansadeler.todolist.mapper.UserMapper;
import com.alicansadeler.todolist.repository.UserRepository;
import com.alicansadeler.todolist.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public BaseResponseDTO<UserResponseDTO> createUser(UserRequestDTO dto) {
        log.info("Creating new user with username: {}", dto.username());

        if(userRepository.existsByUsername(dto.username())) {
            throw new ApiException("Username already exists: " + dto.username(), HttpStatus.CONFLICT);
        }

        User savedUser = userRepository.save(userMapper.toEntity(dto));
        log.info("User created successfully with id: {}", savedUser.getId());
        return new BaseResponseDTO<>(true, "User created successfully", userMapper.toDTO(savedUser));
    }

    @Override
    public BaseResponseDTO<UserResponseDTO> getUserById(int id) {
        log.info("Fetching user with id: {}", id);
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ApiException("User not found with id: " + id, HttpStatus.NOT_FOUND));

        return new BaseResponseDTO<>(true, "User found successfully", userMapper.toDTO(user));
    }

    @Override
    public BaseResponseDTO<List<UserResponseDTO>> getAllUsers() {
        log.info("Fetching all users");
        List<UserResponseDTO> userResponseDTOS = userRepository.findAll().stream()
                .map(userMapper::toDTO)
                .toList();
        log.info("Found {} users", userResponseDTOS.size());
        return new BaseResponseDTO<>(true, "Users listed successfully", userResponseDTOS);
    }

    @Override
    public BaseResponseDTO<String> deleteUser(int id) {
        log.info("Attempting to delete user with id: {}", id);
        if (!userRepository.existsById(id)) {
            throw new ApiException("User not found with id: " + id, HttpStatus.NOT_FOUND);
        }

        userRepository.deleteById(id);
        log.info("User deleted successfully with id: {}", id);
        return new BaseResponseDTO<>(true, "User deleted successfully", null);
    }

    @Override
    public BaseResponseDTO<UserResponseDTO> updateUser(int id, UserRequestDTO userRequestDTO) {
        log.info("Attempting to update user with id: {}", id);
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ApiException("User not found with id: " + id, HttpStatus.NOT_FOUND));

        user.setUsername(userRequestDTO.username());
        user.setPassword(userRequestDTO.password());
        User updatedUser = userRepository.save(user);
        log.info("User updated successfully with id: {}", id);
        return new BaseResponseDTO<>(true, "User updated successfully", userMapper.toDTO(updatedUser));
    }
}