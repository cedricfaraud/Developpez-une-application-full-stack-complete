package com.openclassrooms.mddapi.repository;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import com.openclassrooms.mddapi.model.Post;
import com.openclassrooms.mddapi.model.Topic;

public interface PostRepository extends JpaRepository<Post, Integer> {

    @SuppressWarnings("null")
    List<Post> findAll(Sort sortByDateDesc);

    List<Post> findByTopicIn(List<Topic> topics);

}
