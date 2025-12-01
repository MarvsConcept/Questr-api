package com.marv.questr.services.impl;

import com.marv.questr.domain.Role;
import com.marv.questr.domain.dtos.CreateQuestionRequestDto;
import com.marv.questr.domain.dtos.QuestionDetailDto;
import com.marv.questr.domain.dtos.QuestionSummaryDto;
import com.marv.questr.domain.dtos.UpdateQuestionRequestDto;
import com.marv.questr.domain.entities.Question;
import com.marv.questr.domain.entities.Tag;
import com.marv.questr.domain.entities.User;
import com.marv.questr.domain.repositories.QuestionRepository;
import com.marv.questr.domain.repositories.TagRepository;
import com.marv.questr.domain.repositories.UserRepository;
import com.marv.questr.mappers.QuestionMapper;
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
    private final UserRepository userRepository;
    private final TagRepository tagRepository;


    @Override
    public List<QuestionSummaryDto> getAllQuestions() {
        var questions = questionRepository.findAll();
        return questionMapper.toSummaryDtos(questions);
    }

    @Override
    public QuestionDetailDto getQuestionById(UUID id) {

        var question = questionRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Question does not exist with Id: " + id));

        return questionMapper.toDetailDto(question);
    }

    @Override
    public QuestionDetailDto createQuestion(CreateQuestionRequestDto dto) {

        Question question = questionMapper.toEntity(dto);

        //Get current logged in User
        User currentUser = getCurrentUser();

        question.setAuthor(currentUser);

//        User author = userRepository.findById(dto.getAuthorId())
//                .orElseThrow(() -> new EntityNotFoundException("User not found"));
//        question.setAuthor(author);

        questionRepository.save(question);

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
        User currentUser = getCurrentUser();


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
        User currentUser = getCurrentUser();

        boolean isOwner = question.getAuthor().getId().equals(currentUser.getId());
        boolean isAdmin = currentUser.getRole() == Role.ADMIN;

        if (!isOwner && !isAdmin) {
            throw new IllegalStateException("You are not allowed to delete this question");
        }

        questionRepository.delete(question);
    }



    private User getCurrentUser() {
        return (User) SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal();
    }
}

