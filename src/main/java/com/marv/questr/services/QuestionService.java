package com.marv.questr.services;

import com.marv.questr.domain.dtos.QuestionSummaryDto;
import com.marv.questr.domain.entities.Question;

import java.util.List;

public interface QuestionService {
    List<QuestionSummaryDto> getAllQuestions();


}
