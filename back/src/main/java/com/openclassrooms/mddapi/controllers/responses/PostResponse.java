package com.openclassrooms.mddapi.controllers.responses;

import java.sql.Timestamp;

import com.openclassrooms.mddapi.model.Topic;
import com.openclassrooms.mddapi.model.User;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PostResponse {
    private Integer id;

    private String title;

    private String content;

    private Topic topic;

    private User user;

    private Timestamp createdDate;
}
