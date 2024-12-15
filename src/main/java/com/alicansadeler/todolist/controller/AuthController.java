package com.alicansadeler.todolist.controller;

import com.alicansadeler.todolist.dto.auth.AuthRequest;
import com.alicansadeler.todolist.dto.auth.AuthResponse;
import com.alicansadeler.todolist.paths.ApiUrls;
import com.alicansadeler.todolist.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(ApiUrls.AUTH)
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @Operation(summary = "Kullanıcı Girişi")
    @ApiResponse(responseCode = "200", description = "Başarılı Giriş")
    @ApiResponse(responseCode = "400", description = "Geçersiz giriş bilgisi")
    @PostMapping(ApiUrls.LOGIN)
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody AuthRequest authRequest) {
        return ResponseEntity.ok(authService.login(authRequest));
    }

    @Operation(summary = "Yeni kullanıcı kaydı")
    @ApiResponse(responseCode = "201", description = "Kullanıcı başarıyla oluşturuldu")
    @ApiResponse(responseCode = "400", description = "Geçersiz kullanıcı bilgileri")
    @PostMapping(ApiUrls.REGISTER)
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody AuthRequest authRequest) {
        return ResponseEntity.status(HttpStatus.CREATED).body(authService.register(authRequest));
    }
}
