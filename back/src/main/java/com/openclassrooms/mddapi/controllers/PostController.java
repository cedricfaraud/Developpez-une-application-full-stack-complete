package com.openclassrooms.mddapi.controllers;

import java.util.List;

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

import com.openclassrooms.mddapi.controllers.dto.CommentDto;
import com.openclassrooms.mddapi.controllers.dto.PostDto;
import com.openclassrooms.mddapi.controllers.requests.CommentRequest;
import com.openclassrooms.mddapi.services.PostService;

import io.swagger.v3.oas.annotations.Operation;

@RestController
@RequestMapping("/api/posts")
public class PostController {

    @Autowired
    private PostService postService;

    /**
     * Get all Posts
     * 
     * @return All Posts list
     */
    @GetMapping(value = "/all", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Get list of posts", description = "Retrieve information of all posts")
    public ResponseEntity<List<PostDto>> getPosts() {
        return ResponseEntity.ok(postService.getAllPosts());
    }

    /**
     * Get user subscribed Posts
     * 
     * @return All subscribed Posts list
     */
    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Get list of posts by user Subscribtion", description = "Retrieve information of all user's posts subscribed")
    public ResponseEntity<List<PostDto>> getPostsByUserSubscribtion() {

        List<PostDto> posts = postService.getAllPostsByUserSub();
        return ResponseEntity.ok(posts);
    }

    /**
     * Creates a new post.
     *
     * @param postRequest The post create request containing the post title and
     *                    content.
     * @return ResponseEntity The response entity.
     */
    @PostMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Create post", description = "Create new Post")
    public ResponseEntity<PostDto> createPost(@RequestBody PostDto postRequest) {
        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(postService.createPost(postRequest));
        } catch (Exception e) {
            return new ResponseEntity<PostDto>(HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Get a post by id
     *
     * @param id The ID of the post.
     * @return The specified post.
     */
    @GetMapping("/{id}")
    @Operation(summary = "Get post by id", description = "Retrieve information of specified post by id")
    public ResponseEntity<PostDto> getPostById(@PathVariable("id") Integer id) {
        return ResponseEntity.ok(postService.getPostById(id));
    }

    /**
     * Get comments by post id
     *
     * @param id The ID of the post.
     * @return The comments list
     */
    @GetMapping("/{id}/comments")
    @Operation(summary = "Get post by id", description = "Retrieve information of specified post by id")
    public ResponseEntity<List<CommentDto>> getCommentsByPostId(@PathVariable("id") Integer id) {
        return ResponseEntity.ok(postService.getCommentsByPostId(id));
    }

    /**
     * add comment to the post defined by the id
     * 
     * @param id
     * @param commentRequest
     * @return Created comment
     */
    @PostMapping("/{id}/comment")
    @Operation(summary = "Add comment", description = "Create new comment for a post")
    public ResponseEntity<CommentDto> addPostComment(@PathVariable("id") Integer id,
            @RequestBody CommentRequest commentRequest) {

        return ResponseEntity.ok(postService.createCommentByPostId(id, commentRequest.getContent()));
    }

}