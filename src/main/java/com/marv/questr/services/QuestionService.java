package com.marv.questr.services;

import com.marv.questr.domain.dtos.CreateQuestionRequestDto;
import com.marv.questr.domain.dtos.QuestionDetailDto;
import com.marv.questr.domain.dtos.QuestionSummaryDto;
import com.marv.questr.domain.dtos.UpdateQuestionRequestDto;
import com.marv.questr.domain.entities.Question;

import java.util.List;
import java.util.UUID;

public interface QuestionService {
    List<QuestionSummaryDto> getAllQuestions();
    QuestionDetailDto getQuestionById(UUID id);
    QuestionDetailDto createQuestion(CreateQuestionRequestDto dto);
    QuestionDetailDto updateQuestion(UpdateQuestionRequestDto dto, UUID id);
    void deleteQuestion(UUID id);
}
