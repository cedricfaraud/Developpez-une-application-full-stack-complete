package com.openclassrooms.mddapi.services;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.openclassrooms.mddapi.controllers.dto.TopicDto;
import com.openclassrooms.mddapi.model.Topic;
import com.openclassrooms.mddapi.repository.TopicRepository;

@Service
public class TopicService {
    @Autowired
    private TopicRepository topicRepository;
    @Autowired
    private ModelMapper modelMapper;

    /**
     * 
     * Get all topics.
     *
     * @return list of topics
     */
    public List<TopicDto> getAllTopics() {
        return entityToDtoList(topicRepository.findAll());
    }

    /**
     * Get a topic by ID.
     *
     * @param id ID's topic
     * @return topic with the specified ID
     */
    public TopicDto getTopicById(Integer id) {
        return entityToDto(topicRepository.findById(id).get());
    }

    /**
     * Add new Topic in database
     * 
     * @param topic
     * @return new topic added
     */
    public TopicDto saveTopic(TopicDto topicDto) {
        Topic savedTopic = topicRepository.save(dtoToEntity(topicDto));
        return entityToDto(savedTopic);
    }

    private Topic dtoToEntity(TopicDto topicDto) {
        return modelMapper.map(topicDto, Topic.class);
    }

    private TopicDto entityToDto(Topic topic) {
        return modelMapper.map(topic, TopicDto.class);
    }

    private List<TopicDto> entityToDtoList(List<Topic> topic) {
        return modelMapper.map(topic, new TypeToken<List<TopicDto>>() {
        }.getType());
    }

}
