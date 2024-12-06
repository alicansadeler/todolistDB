package com.alicansadeler.todolist.mapper;

import com.alicansadeler.todolist.dto.UserRequestDTO;
import com.alicansadeler.todolist.dto.UserResponseDTO;
import com.alicansadeler.todolist.entity.User;
import org.springframework.stereotype.Component;


@Component
public class UserMapper {
    public UserResponseDTO toDTO(User user) {
        return new UserResponseDTO(
                user.getId(),
                user.getUsername(),
                user.getCreatedAt()
        );
    }

    public User toEntity(UserRequestDTO dto) {
        User user = new User();
        user.setUsername(dto.username());
        user.setPassword(dto.password());
        return user;
    }
}
