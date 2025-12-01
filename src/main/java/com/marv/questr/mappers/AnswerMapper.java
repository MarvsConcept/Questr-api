package com.marv.questr.mappers;

import com.marv.questr.domain.dtos.AnswerResponseDto;
import com.marv.questr.domain.entities.Answer;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy =  ReportingPolicy.IGNORE)
public interface AnswerMapper {

    @Mapping(target = "authorUsername", source = "author.username")
    AnswerResponseDto toAnswerDto(Answer answer);
}
