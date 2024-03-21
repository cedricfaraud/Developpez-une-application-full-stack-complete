package com.openclassrooms.mddapi.controllers.responses;

import java.util.List;

import com.openclassrooms.mddapi.controllers.dto.PostDto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PostsResponse {
    private List<PostDto> posts;
}
