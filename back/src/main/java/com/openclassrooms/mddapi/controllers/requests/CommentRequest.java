package com.openclassrooms.mddapi.controllers.requests;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CommentRequest {

    @NotNull
    String content;
}
