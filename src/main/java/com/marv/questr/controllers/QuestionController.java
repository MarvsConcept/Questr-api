package com.marv.questr.controllers;

import com.marv.questr.domain.dtos.QuestionSummaryDto;
import com.marv.questr.domain.entities.Question;
import com.marv.questr.services.QuestionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(path = "api/v1/questions")
@RequiredArgsConstructor
public class QuestionController {

    private final QuestionService questionService;

    @GetMapping
    public List<QuestionSummaryDto> getAllQuestions() {
        return questionService.getAllQuestions();
    }
}
