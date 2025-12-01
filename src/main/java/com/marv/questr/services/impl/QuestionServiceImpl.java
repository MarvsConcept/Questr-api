package com.marv.questr.services.impl;

import com.marv.questr.domain.Role;
import com.marv.questr.domain.dtos.*;
import com.marv.questr.domain.entities.Question;
import com.marv.questr.domain.entities.Tag;
import com.marv.questr.domain.entities.User;
import com.marv.questr.domain.repositories.QuestionRepository;
import com.marv.questr.domain.repositories.TagRepository;
import com.marv.questr.domain.repositories.UserRepository;
import com.marv.questr.mappers.AnswerMapper;
import com.marv.questr.mappers.QuestionMapper;
import com.marv.questr.services.CurrentUserService;
import com.marv.questr.services.QuestionService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class QuestionServiceImpl implements QuestionService {

    private final QuestionRepository questionRepository;
    private final QuestionMapper questionMapper;
    private final CurrentUserService currentUserService;
    private final TagRepository tagRepository;
    private final AnswerMapper answerMapper;


    @Override
    public List<QuestionSummaryDto> getAllQuestions() {
        var questions = questionRepository.findAll();
        return questionMapper.toSummaryDtos(questions);
    }

    @Override
    public QuestionDetailDto getQuestionById(UUID id) {

        var question = questionRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Question does not exist with Id: " + id));

        QuestionDetailDto dto = questionMapper.toDetailDto(question);

        List<AnswerResponseDto> answerDtos = question.getAnswers().stream()
                .map(answerMapper::toAnswerDto)
                .toList();

        dto.setAnswers(answerDtos);

        return dto;
    }

//    @Override
//    public QuestionDetailDto createQuestion(CreateQuestionRequestDto dto) {
//
//        Question question = questionMapper.toEntity(dto);
//
//        //Get current logged in User
//        User currentUser = currentUserService.getCurrentUser();
//
//        question.setAuthor(currentUser);
//
//        questionRepository.save(question);
//
//        if (dto.getTags() != null && !dto.getTags().isEmpty()) {
//            var tags = dto.getTags().stream()
//                    .map(tagName -> tagRepository.findByName(tagName)
//                            .orElseGet(() -> tagRepository.save(new Tag(tagName))))
//                    .collect(Collectors.toSet());
//            question.setTags(tags);
//        }
//
//        Question saved = questionRepository.save(question);
//
//        return questionMapper.toDetailDto(saved);
//    }

    @Override
    public QuestionDetailDto createQuestion(CreateQuestionRequestDto dto) {

        // Get current logged-in user
        User currentUser = currentUserService.getCurrentUser();

        // Build the Question entity manually (like Answer)
        Question question = Question.builder()
                .title(dto.getTitle())
                .content(dto.getContent())
                .author(currentUser)
                // views, createdAt, updatedAt will be set by @PrePersist / default
                .build();

        // Handle tags (same logic as before)
        if (dto.getTags() != null && !dto.getTags().isEmpty()) {
            var tags = dto.getTags().stream()
                    .map(tagName -> tagRepository.findByName(tagName)
                            .orElseGet(() -> tagRepository.save(new Tag(tagName))))
                    .collect(Collectors.toSet());
            question.setTags(tags);
        }

        Question saved = questionRepository.save(question);

        return questionMapper.toDetailDto(saved);
    }

    @Override
    @Transactional
    public QuestionDetailDto updateQuestion(UpdateQuestionRequestDto dto, UUID id) {
        Question existingQuestion = questionRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Question does not exist with id: " + id));

        //Get current User
        User currentUser = currentUserService.getCurrentUser();


        boolean isOwner = existingQuestion.getAuthor().getId().equals(currentUser.getId());
        boolean isAdmin = currentUser.getRole() == Role.ADMIN;

        if (!isOwner && !isAdmin) {
            throw new IllegalStateException("You are not allowed to update this question");
        }

        existingQuestion.setTitle(dto.getTitle());
        existingQuestion.setContent(dto.getContent());

        if (dto.getTags() != null) {
            if (dto.getTags().isEmpty()) {
                existingQuestion.getTags().clear(); //remove all
            } else {
                var tags = dto.getTags().stream()
                        .map(tagName -> tagRepository.findByName(tagName)
                                .orElseGet(() -> tagRepository.save(new Tag(tagName))))
                        .collect(Collectors.toSet());
                existingQuestion.setTags(tags);
            }
        }

        Question saved = questionRepository.save(existingQuestion);
        return questionMapper.toDetailDto(saved);
    }

    @Override
    public void deleteQuestion(UUID id) {
        Question question = questionRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Question does not exist with id: " + id));

        //Get current User
        User currentUser = currentUserService.getCurrentUser();

        boolean isOwner = question.getAuthor().getId().equals(currentUser.getId());
        boolean isAdmin = currentUser.getRole() == Role.ADMIN;

        if (!isOwner && !isAdmin) {
            throw new IllegalStateException("You are not allowed to delete this question");
        }

        questionRepository.delete(question);
    }

}

