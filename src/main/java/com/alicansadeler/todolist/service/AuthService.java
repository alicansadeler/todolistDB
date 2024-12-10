package com.alicansadeler.todolist.service;

import com.alicansadeler.todolist.dto.auth.AuthRequest;
import com.alicansadeler.todolist.dto.auth.AuthResponse;

public interface AuthService {
    AuthResponse login(AuthRequest request);
    AuthResponse register(AuthRequest request);
}
