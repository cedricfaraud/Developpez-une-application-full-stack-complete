package com.openclassrooms.mddapi.services;

import java.util.Collections;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.openclassrooms.mddapi.controllers.dto.CommentDto;
import com.openclassrooms.mddapi.controllers.dto.PostDto;
import com.openclassrooms.mddapi.controllers.dto.TopicDto;
import com.openclassrooms.mddapi.controllers.responses.PostResponse;
import com.openclassrooms.mddapi.model.Comment;
import com.openclassrooms.mddapi.model.Post;
import com.openclassrooms.mddapi.model.Topic;
import com.openclassrooms.mddapi.model.User;
import com.openclassrooms.mddapi.repository.CommentRepository;
import com.openclassrooms.mddapi.repository.PostRepository;

@Service
public class PostService {

    @Autowired
    private PostRepository postRepository;
    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private TopicService topicService;
    @Autowired
    private UserService userService;

    /**
     * 
     * Get all posts.
     *
     * @return list of posts
     */
    public List<PostDto> getAllPosts() {
        return postEntityToDtoList(postRepository.findAll());
    }

    /**
     * Retrieves all posts sorted by created date in descending order.
     *
     * @return the list of posts
     */
    public List<PostDto> getAllPostsByUserSub() {

        List<TopicDto> topics = userService.getUserSubscription();
        if (topics.isEmpty()) {
            return Collections.emptyList();
        }
        return postEntityToDtoList(postRepository.findByTopicIn(topicDtoToEntityList(topics)));
    }

    /**
     * Get post by Id
     * 
     * @param id
     * @return post with the specified ID
     */
    public PostDto getPostById(Integer id) {
        Post post = postRepository.findById(id).get();
        PostResponse response = new PostResponse();
        response.setId(post.getId());
        response.setTitle(post.getTitle());
        response.setContent(post.getContent());
        response.setUser(post.getUser());
        response.setTopic(post.getTopic());
        response.setCreatedDate(post.getCreatedAt());
        // return response;
        return postEntityToDto(post);
    }

    /**
     * This method is used to create a new post.
     *
     * @param post This is the post to save
     * @return the created post.
     */
    public PostDto createPost(PostDto post) {
        Post newPost = new Post();

        // get current logged user
        User usr = userService.getUser();
        // get topic by id
        Topic topic = topicDtoToEntity(topicService.getTopicById(post.getTopicId()));

        newPost.setUser(usr);
        newPost.setTopic(topic);
        newPost.setTitle(post.getTitle());
        newPost.setContent(post.getContent());
        return postEntityToDto(postRepository.save(newPost));
    }

    /**
     * Create new comment
     * 
     * @param id      the id of the post
     * @param content the comment content
     * @return created comment
     */
    public CommentDto createCommentByPostId(Integer id, String content) {
        Comment newComment = new Comment();
        newComment.setContent(content);
        newComment.setUser(userService.getUser());
        newComment.setPost(postDtoToEntity(getPostById(id)));

        return commentEntityToDto(commentRepository.save(newComment));
    }

    /**
     * get all comments by post id
     * 
     * @param id of the commented post
     * @return all comments for the post id
     */
    public List<CommentDto> getCommentsByPostId(Integer id) {
        return commentDtoToEntityList(commentRepository.findByPostId(id));
    }

    private Post postDtoToEntity(PostDto postDto) {
        return modelMapper.map(postDto, Post.class);
    }

    private PostDto postEntityToDto(Post post) {
        return modelMapper.map(post, PostDto.class);
    }

    private List<PostDto> postEntityToDtoList(List<Post> posts) {
        return modelMapper.map(posts, new TypeToken<List<PostDto>>() {
        }.getType());
    }

    private Topic topicDtoToEntity(TopicDto topic) {
        return modelMapper.map(topic, Topic.class);
    }

    private List<Topic> topicDtoToEntityList(List<TopicDto> topics) {
        return modelMapper.map(topics, new TypeToken<List<Topic>>() {
        }.getType());
    }

    private List<CommentDto> commentDtoToEntityList(List<Comment> comments) {
        return modelMapper.map(comments, new TypeToken<List<CommentDto>>() {
        }.getType());
    }

    private CommentDto commentEntityToDto(Comment comment) {
        CommentDto commentDto = new CommentDto();
        commentDto.setContent(comment.getContent());
        commentDto.setId(comment.getId());
        commentDto.setUserName(comment.getUser().getName());

        return commentDto;
    }
}
