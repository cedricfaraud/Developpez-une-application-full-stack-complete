package com.openclassrooms.mddapi.controllers.dto;

import java.util.List;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class TopicDto {
    private Long id;

    @NotBlank
    private String title;

    @NotNull
    private String description;

    private List<PostDto> posts;
}
