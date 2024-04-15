package com.openclassrooms.mddapi.controllers;

import java.util.List;
import java.util.NoSuchElementException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.openclassrooms.mddapi.controllers.dto.TopicDto;
import com.openclassrooms.mddapi.services.TopicService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/topics")
@Tag(name = "Topic")
public class TopicController {
    public static final Logger logger = LoggerFactory.getLogger(TopicController.class);
    @Autowired
    private TopicService topicService;

    /**
     * Get all Topics
     * 
     * @return All Topics list
     */
    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Get list of topics", description = "Retrieve information of all topics")
    public ResponseEntity<List<TopicDto>> getTopics() {

        List<TopicDto> topics = topicService.getAllTopics();
        return ResponseEntity.ok(topics);
    }

    /**
     * Creates a new topic.
     *
     * @param topicRequest topic's create request containing the topic title and
     *                     content.
     * @return topic creadted json format
     */
    @PostMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Create a new topic", description = "Create a new topic for the authenticated user.")
    public ResponseEntity<TopicDto> createTopic(@RequestBody TopicDto topicRequest) {
        try {

            TopicDto topic = topicService.saveTopic(topicRequest);
            return ResponseEntity.status(HttpStatus.CREATED).body(topic);
        } catch (NumberFormatException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Get user by Id
     * 
     * @param id
     * @return user
     */
    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Get topic by id", description = "Retrieve informations about topic specified by his id")
    public ResponseEntity<TopicDto> getTopicById(@PathVariable Integer id) {
        try {
            TopicDto topic = topicService.getTopicById(id);
            return new ResponseEntity<TopicDto>(topic, HttpStatus.OK);
        } catch (NoSuchElementException e) {
            return new ResponseEntity<TopicDto>(HttpStatus.NOT_FOUND);
        }
    }
}
