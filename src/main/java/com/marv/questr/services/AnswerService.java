package com.marv.questr.services;

import com.marv.questr.domain.dtos.AnswerResponseDto;
import com.marv.questr.domain.dtos.CreateAnswerRequestDto;

import java.util.UUID;

public interface AnswerService {

    AnswerResponseDto createAnswer(UUID questionId, CreateAnswerRequestDto dto);

}
