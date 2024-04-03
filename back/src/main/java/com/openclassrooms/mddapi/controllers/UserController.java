package com.openclassrooms.mddapi.controllers;

import java.util.List;
import java.util.NoSuchElementException;

import org.apache.coyote.BadRequestException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.openclassrooms.mddapi.controllers.dto.TopicDto;
import com.openclassrooms.mddapi.controllers.responses.TopicsResponse;
import com.openclassrooms.mddapi.model.User;
import com.openclassrooms.mddapi.services.TopicService;
import com.openclassrooms.mddapi.services.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/user")
@Tag(name = "User")
public class UserController {
    public static final Logger logger = LoggerFactory.getLogger(UserController.class);
    @Autowired
    private UserService userService;
    @Autowired
    private TopicService topicService;

    /**
     * Get user by Id
     * 
     * @param id
     * @return user
     */
    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Get user by id", description = "Retrieve account information about user specified by his id")
    public ResponseEntity<User> getUserById(@PathVariable Integer id) {
        try {
            User user = userService.getUser(id);
            return new ResponseEntity<User>(user, HttpStatus.OK);
        } catch (NoSuchElementException e) {
            return new ResponseEntity<User>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Subscribes the authenticated user to a specified topic.
     *
     * @param topic_id The ID of the topic to subscribe to.
     * @return ResponseEntity The response entity.
     */
    @PostMapping("/{id}/subscribe")
    @Operation(summary = "Subscribe to a topic", description = "Subscribes the authenticated user to a specified topic.")
    public ResponseEntity<TopicsResponse> subscribe(@PathVariable("id") Integer topic_id) {
        try {
            userService.subscribeTopic(topic_id);
            List<TopicDto> topics = topicService.getAllTopics();
            return ResponseEntity.ok(new TopicsResponse(topics));

        } catch (BadRequestException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     *
     * Unsubscribes topic for current user
     *
     * @param topic_id The ID of the topic to unsubscribe
     * @return subscribed topics
     */
    @DeleteMapping("/{id}/unsubscribe")
    @Operation(summary = "Unsubscribe to a topic", description = "Unsubscribes the authenticated user to a specified topic.")
    public ResponseEntity<TopicsResponse> unsubscribe(@PathVariable("id") Integer topic_id) {
        try {
            this.userService.unsubscribeTopic(topic_id);
            List<TopicDto> topics = topicService.getAllTopics();
            return ResponseEntity.ok(new TopicsResponse(topics));

        } catch (BadRequestException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Get user's topics subsriptions
     *
     * @return The ResponseEntity containing the list of user's topics
     */
    @GetMapping("/subscription")
    @Operation(summary = "Get all user's topics", description = "Retrieve topics subscribed by authenticated user")
    // public ResponseEntity<TopicsResponse> getUserSubscription() {
    public ResponseEntity<List<TopicDto>> getUserSubscription() {

        List<TopicDto> topics = userService.getUserSubscription();

        // return ResponseEntity.ok(new TopicsResponse(topics));
        return ResponseEntity.ok(topics);
    }

}
