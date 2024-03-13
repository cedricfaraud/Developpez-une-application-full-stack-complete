package com.openclassrooms.mddapi.controllers.dto;

import java.util.Date;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CommentDto {

    private Long id;

    @NotNull
    private Date date;

    @NotBlank
    private String content;

    @NotBlank
    private UserDto user;

}
