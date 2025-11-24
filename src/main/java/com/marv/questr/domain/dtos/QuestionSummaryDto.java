package com.marv.questr.domain.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class QuestionSummaryDto {

    private UUID id;

    private String title;

    private String excerpt;

    private LocalDateTime createdAt;

    private Integer views;

    private String authorUsername;

    private Set<String> tags;

}
