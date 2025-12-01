package com.marv.questr.domain.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AnswerResponseDto {

    private UUID id;

    private String content;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private String authorUsername;

}
