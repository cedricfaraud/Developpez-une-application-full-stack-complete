package com.openclassrooms.mddapi.controllers.dto;

import java.sql.Timestamp;

import com.openclassrooms.mddapi.model.Topic;
import com.openclassrooms.mddapi.model.User;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class PostDto {

    private Integer id;

    @NotBlank
    private Integer topicId;

    @NotBlank
    @Size(max = 50)
    private String title;

    @NotBlank
    private String content;

    private User user;
    private Topic topic;

    private Timestamp createdAt;
}
