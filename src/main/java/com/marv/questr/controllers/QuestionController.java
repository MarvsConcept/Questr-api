package com.marv.questr.controllers;

import com.marv.questr.domain.dtos.CreateQuestionRequestDto;
import com.marv.questr.domain.dtos.QuestionDetailDto;
import com.marv.questr.domain.dtos.QuestionSummaryDto;
import com.marv.questr.domain.dtos.UpdateQuestionRequestDto;
import com.marv.questr.domain.entities.Question;
import com.marv.questr.services.QuestionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(path = "api/v1/questions")
@RequiredArgsConstructor
public class QuestionController {

    private final QuestionService questionService;

    @GetMapping
    public List<QuestionSummaryDto> getAllQuestions() {
        return questionService.getAllQuestions();
    }

    @GetMapping("/{id}")
    public QuestionDetailDto getQuestionById(@PathVariable UUID id) {
        return questionService.getQuestionById(id);
    }

    @PostMapping
    public QuestionDetailDto createQuestion(
            @Valid @RequestBody CreateQuestionRequestDto dto) {
        return questionService.createQuestion(dto);
    }

    @PutMapping("/{id}")
    public QuestionDetailDto updateQuestion(
            @PathVariable UUID id,
            @Valid @RequestBody UpdateQuestionRequestDto dto) {
        return questionService.updateQuestion(dto, id);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteQuestion(
            @PathVariable UUID id) {
        questionService.deleteQuestion(id);
    }
}
