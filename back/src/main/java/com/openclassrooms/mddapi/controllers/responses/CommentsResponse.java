package com.openclassrooms.mddapi.controllers.responses;

import java.util.List;

import com.openclassrooms.mddapi.controllers.dto.CommentDto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CommentsResponse {
    private List<CommentDto> comments;
}
