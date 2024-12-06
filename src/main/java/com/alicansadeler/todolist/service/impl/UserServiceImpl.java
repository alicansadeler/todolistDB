package com.alicansadeler.todolist.service.impl;

import com.alicansadeler.todolist.dto.BaseResponseDTO;
import com.alicansadeler.todolist.dto.UserRequestDTO;
import com.alicansadeler.todolist.dto.UserResponseDTO;
import com.alicansadeler.todolist.entity.User;
import com.alicansadeler.todolist.mapper.UserMapper;
import com.alicansadeler.todolist.repository.UserRepository;
import com.alicansadeler.todolist.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public BaseResponseDTO<UserResponseDTO> createUser(UserRequestDTO dto) {
        if(userRepository.existsByUsername(dto.username())) {
            return new BaseResponseDTO<>(false, "Username already exists", null);
        }
        User savedUser = userRepository.save(userMapper.toEntity(dto));
        return new BaseResponseDTO<>(true, "User created successfully", userMapper.toDTO(savedUser));
    }


    @Override
    public BaseResponseDTO<UserResponseDTO> getUserById(int id) {
        return userRepository.findById(id)
                .map(user -> new BaseResponseDTO<>(true, "User found successfully", userMapper.toDTO(user)))
                .orElse(new BaseResponseDTO<>(false, "User not found", null));
    }

    @Override
    public BaseResponseDTO<List<UserResponseDTO>> getAllUsers() {
        List<UserResponseDTO> userResponseDTOS = userRepository.findAll().stream()
                .map(userMapper::toDTO)
                .toList();
        return new BaseResponseDTO<>(true, "Users listed successfully", userResponseDTOS);
    }

    @Override
    public BaseResponseDTO<String> deleteUser(int id) {
        if (!userRepository.existsById(id)) {
            return new BaseResponseDTO<>(false, "User not found", null);
        }

        userRepository.deleteById(id);
        return new BaseResponseDTO<>(true, "User deleted successfully", null);
    }

    @Override
    public BaseResponseDTO<UserResponseDTO> updateUser(int id, UserRequestDTO request) {
        return userRepository.findById(id)
                .map(user -> {
                    user.setUsername(request.username());
                    user.setPassword(request.password());
                    User updatedUser = userRepository.save(user);
                    return new BaseResponseDTO<>(
                            true,
                            "User updated successfully",
                            userMapper.toDTO(updatedUser)
                    );
                })
                .orElse(new BaseResponseDTO<>(
                        false,
                        "User not found",
                        null
                ));
    }
}
