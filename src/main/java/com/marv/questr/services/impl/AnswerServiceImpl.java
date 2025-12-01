package com.marv.questr.services.impl;

import com.marv.questr.domain.dtos.AnswerResponseDto;
import com.marv.questr.domain.dtos.CreateAnswerRequestDto;
import com.marv.questr.domain.entities.Answer;
import com.marv.questr.domain.entities.Question;
import com.marv.questr.domain.entities.User;
import com.marv.questr.domain.repositories.AnswerRepository;
import com.marv.questr.domain.repositories.QuestionRepository;
import com.marv.questr.domain.repositories.UserRepository;
import com.marv.questr.mappers.AnswerMapper;
import com.marv.questr.services.AnswerService;
import com.marv.questr.services.CurrentUserService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AnswerServiceImpl implements AnswerService {

    private final UserRepository userRepository;
    private final QuestionRepository questionRepository;
    private final CurrentUserService currentUserService;
    private final AnswerRepository answerRepository;
    private final AnswerMapper answerMapper;

    @Override
    public AnswerResponseDto createAnswer(UUID questionId, CreateAnswerRequestDto dto) {

        // Fetch the question
        Question question = questionRepository.findById(questionId)
                .orElseThrow(() -> new EntityNotFoundException("Question does not exist with id: " + questionId));

        // Get the current User
        User currentUser = currentUserService.getCurrentUser();

        // Create new answer
        Answer answer = Answer.builder()
                .content(dto.getContent())
                .author(currentUser)
                .question(question)
                .build();

        // Save Answer

        Answer savedAnswer = answerRepository.save(answer);

        return answerMapper.toAnswerDto(savedAnswer);
    }

}
