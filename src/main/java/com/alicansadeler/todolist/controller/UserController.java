package com.alicansadeler.todolist.controller;

import com.alicansadeler.todolist.dto.BaseResponseDTO;
import com.alicansadeler.todolist.dto.UserRequestDTO;
import com.alicansadeler.todolist.dto.UserResponseDTO;
import com.alicansadeler.todolist.paths.ApiUrls;
import com.alicansadeler.todolist.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(ApiUrls.USERS)
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping
    public ResponseEntity<BaseResponseDTO<UserResponseDTO>> post(@Valid @RequestBody UserRequestDTO userRequestDTO) {
        BaseResponseDTO<UserResponseDTO> responseDTO = userService.createUser(userRequestDTO);
        return ResponseEntity.status(responseDTO.isSuccess() ? HttpStatus.CREATED : HttpStatus.BAD_REQUEST)
                .body(responseDTO);
    }

    @GetMapping(ApiUrls.ID)
    public ResponseEntity<BaseResponseDTO<UserResponseDTO>> getUser(@Valid @PathVariable int id) {
        BaseResponseDTO<UserResponseDTO> responseDTO = userService.getUserById(id);
        return ResponseEntity.status(responseDTO.isSuccess() ? HttpStatus.OK : HttpStatus.NOT_FOUND).body(responseDTO);
    }

    @GetMapping
    public ResponseEntity<BaseResponseDTO<List<UserResponseDTO>>> getAllUser() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @PutMapping(ApiUrls.ID)
    public ResponseEntity<BaseResponseDTO<UserResponseDTO>> putUser(@PathVariable int id, @Valid @RequestBody UserRequestDTO userRequestDTO) {
        BaseResponseDTO<UserResponseDTO> baseResponseDTO = userService.updateUser(id, userRequestDTO);
        return ResponseEntity.status(baseResponseDTO.isSuccess() ? HttpStatus.OK : HttpStatus.NOT_FOUND).body(baseResponseDTO);
    }

    @DeleteMapping(ApiUrls.ID)
    public ResponseEntity<BaseResponseDTO<String>> deleteUser(@PathVariable int id) {
        BaseResponseDTO<String> baseResponseDTO = userService.deleteUser(id);
        return ResponseEntity.status(baseResponseDTO.isSuccess() ? HttpStatus.OK : HttpStatus.NOT_FOUND)
                .body(baseResponseDTO);
    }
}
