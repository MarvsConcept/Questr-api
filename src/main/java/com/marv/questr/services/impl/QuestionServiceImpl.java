package com.marv.questr.services.impl;

import com.marv.questr.domain.dtos.QuestionSummaryDto;
import com.marv.questr.domain.entities.Question;
import com.marv.questr.domain.entities.Tag;
import com.marv.questr.domain.repositories.QuestionRepository;
import com.marv.questr.mappers.QuestionMapper;
import com.marv.questr.services.QuestionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class QuestionServiceImpl implements QuestionService {

    private final QuestionRepository questionRepository;
    private final QuestionMapper questionMapper;

    @Override
    public List<QuestionSummaryDto> getAllQuestions() {
        var questions = questionRepository.findAll();
        return questionMapper.toSummaryDtos(questions);
    }
}
