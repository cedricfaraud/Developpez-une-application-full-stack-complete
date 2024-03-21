package com.openclassrooms.mddapi.controllers.responses;

import java.util.List;

import com.openclassrooms.mddapi.controllers.dto.TopicDto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TopicsResponse {
    private List<TopicDto> topic;
}
