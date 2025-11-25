package com.marv.questr.domain.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateQuestionRequestDto {

    @NotBlank(message = "Title is required")
    @Size(max = 200, message = "Title must be at most {max} characters")
    private String title;

    @NotBlank(message = "Content is required")
    private String content;

    private Set<String> tags;
}
