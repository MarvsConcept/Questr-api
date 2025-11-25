package com.marv.questr.domain.dtos;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateQuestionRequestDto {

    @NotBlank(message = "Title is required")
    @Size(max = 200, message = "Title must be at most {max} characters")
    private String title;

    @NotBlank(message = "Content is required")
    private String content;

    @NotNull(message = "Author id is required")
    private UUID authorId;

    private Set<String> tags;
}
