package com.marv.questr.controllers;

import com.marv.questr.domain.dtos.LoginRequestDto;
import com.marv.questr.domain.dtos.LoginResponseDto;
import com.marv.questr.domain.dtos.RegisterRequestDto;
import com.marv.questr.services.AuthService;
import jakarta.validation.Valid;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public void register(
            @Valid @RequestBody RegisterRequestDto dto) {
        authService.register(dto);
    }

    @PostMapping("/login")
    public LoginResponseDto login(
            @Valid @RequestBody LoginRequestDto dto) {
        return authService.login(dto);
    }
}
