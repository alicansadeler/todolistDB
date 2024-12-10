package com.alicansadeler.todolist.controller;

import com.alicansadeler.todolist.dto.auth.AuthResponse;
import com.alicansadeler.todolist.paths.ApiUrls;
import com.alicansadeler.todolist.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping(ApiUrls.LOGIN)
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody AuthResponse authResponse) {
        return ResponseEntity.ok(authResponse);
    }

    @PostMapping(ApiUrls.REGISTER)
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody AuthResponse authResponse) {
        return ResponseEntity.status(HttpStatus.CREATED).body(authResponse);
    }
}
