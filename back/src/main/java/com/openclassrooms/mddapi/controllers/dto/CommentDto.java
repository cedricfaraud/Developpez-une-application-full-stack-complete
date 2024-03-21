package com.openclassrooms.mddapi.controllers.dto;

import java.sql.Timestamp;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CommentDto {

    private Long id;

    @NotBlank
    private String content;

    @NotBlank
    private PostDto post;

    @NotBlank
    private UserDto user;

    @NotNull
    private Timestamp createdDate;

}
