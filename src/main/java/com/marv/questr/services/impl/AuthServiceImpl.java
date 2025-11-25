package com.marv.questr.services.impl;

import com.marv.questr.domain.dtos.LoginRequestDto;
import com.marv.questr.domain.dtos.LoginResponseDto;
import com.marv.questr.domain.dtos.RegisterRequestDto;
import com.marv.questr.domain.entities.User;
import com.marv.questr.domain.repositories.UserRepository;
import com.marv.questr.security.JwtService;
import com.marv.questr.services.AuthService;
import lombok.RequiredArgsConstructor;
import org.hibernate.validator.constraints.pl.REGON;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    @Override
    public void register(RegisterRequestDto dto) {

        if (userRepository.findByEmail(dto.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Email is already in use");
        }

        if (userRepository.findByUsername(dto.getUsername()).isPresent()) {
            throw new IllegalArgumentException("Username is already in use");
        }

        String hashedPassword = passwordEncoder.encode(dto.getPassword());

        User user = User.builder()
                .username(dto.getUsername())
                .email(dto.getEmail())
                .password(hashedPassword)
                .build();

        userRepository.save(user);
    }

    @Override
    public LoginResponseDto login(LoginRequestDto dto) {
        User user = userRepository.findByEmail(dto.getEmail())
                .orElseThrow(()-> new BadCredentialsException("Invalid Email or Password"));

        boolean passwordMatches = passwordEncoder.matches(dto.getPassword(), user.getPassword());

        if (!passwordMatches) {
            throw new BadCredentialsException("Invalid Email or Password");
        }

        String token = jwtService.generateToken(user);

        return LoginResponseDto.builder()
                .token(token)
                .build();

    }
}
