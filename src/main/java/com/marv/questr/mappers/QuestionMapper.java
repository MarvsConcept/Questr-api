package com.marv.questr.mappers;


import com.marv.questr.domain.dtos.CreateQuestionRequestDto;
import com.marv.questr.domain.dtos.QuestionDetailDto;
import com.marv.questr.domain.dtos.QuestionSummaryDto;
import com.marv.questr.domain.entities.Question;
import com.marv.questr.domain.entities.Tag;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface QuestionMapper {

    @Mapping(target = "authorUsername", source = "author.username")
    @Mapping(target = "tags", expression = "java(mapTags(question.getTags()))")
    @Mapping(target = "excerpt", ignore = true) // excerpt is filled in @AfterMapping

    QuestionSummaryDto toSummaryDto(Question question);
    List<QuestionSummaryDto> toSummaryDtos(List<Question> questions);

    default Set<String> mapTags(Set<Tag> tags) {
        if (tags == null) {
            return Set.of();
        }
        return tags.stream()
                .map(Tag::getName)
                .collect(Collectors.toSet());
    }

    @AfterMapping
    default void addExcerpt(@MappingTarget QuestionSummaryDto dto, Question question) {
        String content = question.getContent();
        if (content == null) {
            dto.setExcerpt("");
            return;
        }
        if (content.length() > 150) {
            dto.setExcerpt(content.substring(0, 150) + "...");
        } else {
            dto.setExcerpt(content);
        }
    }

    @Mapping(target = "authorUsername", source = "author.username")
    @Mapping(target = "tags", expression = "java(mapTags(question.getTags()))")
    @Mapping(target = "answers", ignore = true)
    QuestionDetailDto toDetailDto(Question question);


    @Mapping(target = "id", ignore = true)
    @Mapping(target = "views", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "author", ignore = true) // will be set in service
    @Mapping(target = "tags", ignore = true)   // will be set in service
    Question toEntity(CreateQuestionRequestDto dto);

}
