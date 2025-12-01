package com.marv.questr.controllers;

import com.marv.questr.domain.dtos.AnswerResponseDto;
import com.marv.questr.domain.dtos.CreateAnswerRequestDto;
import com.marv.questr.services.AnswerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/questions/{questionId}/answers")
@RequiredArgsConstructor
public class AnswerController {

    private final AnswerService answerService;

    @PostMapping
    public AnswerResponseDto createAnswer(
            @PathVariable UUID questionId,
            @Valid @RequestBody CreateAnswerRequestDto dto
            ) {
        return answerService.createAnswer(questionId, dto);
    }
}
