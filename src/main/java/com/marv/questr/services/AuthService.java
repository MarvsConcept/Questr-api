package com.marv.questr.services;

import com.marv.questr.domain.dtos.AdminRegisterRequestDto;
import com.marv.questr.domain.dtos.LoginRequestDto;
import com.marv.questr.domain.dtos.LoginResponseDto;
import com.marv.questr.domain.dtos.RegisterRequestDto;

public interface AuthService {
    void register(RegisterRequestDto dto);

    LoginResponseDto login(LoginRequestDto dto);

    void registerAdmin(AdminRegisterRequestDto dto);


}


