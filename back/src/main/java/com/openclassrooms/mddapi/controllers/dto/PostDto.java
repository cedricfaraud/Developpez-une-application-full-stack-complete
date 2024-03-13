package com.openclassrooms.mddapi.controllers.dto;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class PostDto {
    private Long id;

    @NotBlank
    @Size(max = 50)
    private String title;

    private UserDto user;

    private Date date;

    @NotBlank
    private String content;

    private TopicDto topic;

    private List<CommentDto> comments = new ArrayList<>();
}
