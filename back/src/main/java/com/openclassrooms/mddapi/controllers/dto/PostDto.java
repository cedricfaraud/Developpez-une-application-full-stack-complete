package com.openclassrooms.mddapi.controllers.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class PostDto {

    @NotBlank
    private Integer topicId;

    @NotBlank
    @Size(max = 50)
    private String title;

    @NotBlank
    private String content;
}
