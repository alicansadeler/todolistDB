package com.alicansadeler.todolist.repository;

import com.alicansadeler.todolist.dto.UserResponseDTO;
import com.alicansadeler.todolist.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.security.Timestamp;
import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByUsername(String username);
    List<User> findByCreatedAtAfter(Timestamp date);
    boolean existsByUsername(String username);
}
