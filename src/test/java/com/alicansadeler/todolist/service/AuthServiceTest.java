package com.alicansadeler.todolist.service;

import com.alicansadeler.todolist.dto.auth.AuthRequest;
import com.alicansadeler.todolist.dto.auth.AuthResponse;
import com.alicansadeler.todolist.entity.User;
import com.alicansadeler.todolist.exceptions.ApiException;
import com.alicansadeler.todolist.repository.UserRepository;
import com.alicansadeler.todolist.service.impl.AuthServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtService jwtService;

    @Mock
    private AuthenticationManager authenticationManager;

    @InjectMocks
    private AuthServiceImpl authService;

    @Test
    void girisYap_GecerliKullaniciBilgileriyle_BasariliOlmali() {
        // Given
        String username = "testUser";
        String password = "testPass";
        AuthRequest request = new AuthRequest(username, password);
        User mockUser = new User();
        mockUser.setUsername(username);
        String mockToken = "testToken";

        when(userRepository.findByUsername(username)).thenReturn(Optional.of(mockUser));
        when(jwtService.generateToken(mockUser)).thenReturn(mockToken);

        // When
        AuthResponse response = authService.login(request);

        // Then
        assertNotNull(response);
        assertEquals(username, response.username());
        assertEquals(mockToken, response.token());
        verify(authenticationManager).authenticate(any());
        verify(userRepository).findByUsername(username);
        verify(jwtService).generateToken(mockUser);
    }

    @Test
    void girisYap_KullaniciBulunamadiginda_HataFirlatmali() {
        // Given
        String username = "test";
        String password = "testla";
        AuthRequest request = new AuthRequest(username, password);

        when(userRepository.findByUsername(username)).thenReturn(Optional.empty());

        // When/Then
        assertThrows(ApiException.class, () -> authService.login(request));
        verify(userRepository).findByUsername(username);  // "nonexistent" yerine username
    }

    @Test
    void kayitOl_GecerliKullaniciBilgileriyle_BasariliOlmali() {
        // Given
        String username = "yeniKullanici";
        String password = "sifre123";
        AuthRequest request = new AuthRequest(username, password);
        User yeniKullanici = new User();
        yeniKullanici.setUsername(username);
        String token = "yeniToken";

        when(userRepository.existsByUsername(username)).thenReturn(false);
        when(passwordEncoder.encode(password)).thenReturn("sifreliSifre");
        when(userRepository.save(any())).thenReturn(yeniKullanici);
        when(jwtService.generateToken(any())).thenReturn(token);

        // When
        AuthResponse response = authService.register(request);

        // Then
        assertNotNull(response);
        assertEquals(username, response.username());
        assertEquals(token, response.token());
        verify(userRepository).existsByUsername(username);
        verify(passwordEncoder).encode(password);
        verify(userRepository).save(any());
        verify(jwtService).generateToken(any());
    }

    @Test
    void kayitOl_KullaniciAdiMevcutsa_HataFirlatmali() {
        // Given
        String username = "mevcutKullanici";
        String password = "sifre123";
        AuthRequest request = new AuthRequest(username, password);

        when(userRepository.existsByUsername(username)).thenReturn(true);

        // When/Then
        assertThrows(ApiException.class, () -> authService.register(request));
        verify(userRepository).existsByUsername(username);
    }
}
