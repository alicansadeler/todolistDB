package com.alicansadeler.todolist.service.impl;

import com.alicansadeler.todolist.dto.auth.AuthRequest;
import com.alicansadeler.todolist.dto.auth.AuthResponse;
import com.alicansadeler.todolist.entity.User;
import com.alicansadeler.todolist.exceptions.ApiException;
import com.alicansadeler.todolist.repository.UserRepository;
import com.alicansadeler.todolist.service.AuthService;
import com.alicansadeler.todolist.service.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;


    @Override
    public AuthResponse login(AuthRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.username(), request.password())
        );
        User user = userRepository.findByUsername(request.username())
                .orElseThrow(() -> new ApiException("User not found", HttpStatus.NOT_FOUND));
        String token = jwtService.generateToken(user);
        return new AuthResponse(token, user.getUsername());
    }

    @Override
    public AuthResponse register(AuthRequest request) {
        if (userRepository.existsByUsername(request.username())) {
            throw new ApiException("Username already exists", HttpStatus.CONFLICT);
        }

        User user = new User();
        user.setUsername(request.username());
        user.setPassword(request.password());
        userRepository.save(user);
        String token = jwtService.generateToken(user);

        return new AuthResponse(token, user.getUsername());
    }
}
