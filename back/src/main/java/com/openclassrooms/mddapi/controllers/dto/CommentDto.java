package com.openclassrooms.mddapi.controllers.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CommentDto {

    private Integer id;

    @NotBlank
    private String content;

    @NotBlank
    private String userName;
}
