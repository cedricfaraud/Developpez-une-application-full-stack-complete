package com.openclassrooms.mddapi.services;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

import org.apache.coyote.BadRequestException;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.openclassrooms.mddapi.controllers.LoginController;
import com.openclassrooms.mddapi.controllers.dto.TopicDto;
import com.openclassrooms.mddapi.controllers.dto.UserDto;
import com.openclassrooms.mddapi.model.Topic;
import com.openclassrooms.mddapi.model.User;
import com.openclassrooms.mddapi.repository.TopicRepository;
import com.openclassrooms.mddapi.repository.UserRepository;

import lombok.Data;

@Data
@Service
public class UserService implements UserDetailsService {
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder bcryptEncoder;
    @Autowired
    private TopicRepository topicRepository;
    @Autowired
    private ModelMapper modelMapper;

    /**
     * get user by id
     * 
     * @param id
     * @return user found
     */
    public User getUser(final Integer id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Bad id : " + id));
    }

    /**
     * get user by email
     * 
     * @param email
     * @return user found
     */
    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new NoSuchElementException("Bad mail : " + email));
    }

    /**
     * Login user
     * 
     * @param email    user email
     * @param password user password
     * @return logged user
     */
    public UserDto userLogin(String email, String password) {
        User userByEmail = getUserByEmail(email);
        logger.info("Login user found : " + userByEmail);
        if (!bcryptEncoder.matches(password, userByEmail.getPassword())) {
            throw new BadCredentialsException("Bad password");
        }

        return entityToDto(userByEmail);
    }

    /**
     * delete user
     * 
     * @param id user id
     */
    public void deleteUser(final Integer id) {
        userRepository.deleteById(id);
    }

    /**
     * Save user
     * 
     * @param userDto user to save
     * @return saved user
     */
    public UserDto saveUser(UserDto userDto) {
        User savedUser = userRepository.save(dtoToEntity(userDto));
        return entityToDto(savedUser);
    }

    /**
     * update user
     * 
     * @param userDto user to update
     * @return user updated
     */
    public UserDto updateUser(UserDto userDto) {
        User user = getUser();
        if (isNotNullOrEmpty(userDto.getPassword())) {
            user.setPassword(bcryptEncoder.encode(userDto.getPassword()));
        }
        if (isNotNullOrEmpty(userDto.getEmail())) {
            user.setEmail(userDto.getEmail());
        }
        if (isNotNullOrEmpty(userDto.getName())) {
            user.setName(userDto.getName());
        }
        User updatedUser = userRepository.save(user);
        return entityToDto(updatedUser);
    }

    /**
     * Retrieves topics list from the authenticated user.
     *
     * @return User's Topics list.
     */
    public List<TopicDto> getUserSubscription() {
        User user = getUser();
        List<Topic> subscriptions = user.getTopics();
        return entityToDtoList(subscriptions);
    }

    @Override
    public UserDetails loadUserByUsername(String name) throws UsernameNotFoundException {
        User user = userRepository.findByName(name);
        return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(),
                getGrantedAuthorities());
    }

    /**
     * Add autenticated user subscribe to topic
     *
     * @param topic_id the topic id to subscribe.
     * @throws BadRequestException
     */
    public void subscribeTopic(Integer topic_id) throws BadRequestException {
        /** Retrieves the authenticated user. */
        User user = getUser();
        Topic topic = this.topicRepository.findById(topic_id).orElse(null);

        if (topic == null || user == null) {
            throw new NoSuchElementException();
        }

        boolean alreadySubscribe = user.getTopics().stream().anyMatch(o -> o.getId().equals(topic_id));
        if (alreadySubscribe) {
            throw new BadRequestException();
        }

        user.getTopics().add(topic);

        this.userRepository.save(user);
    }

    /**
     * Remove Topic subscription of authenticated user.
     *
     * @param topic_id the topic id to unsubscribe.
     * @throws BadRequestException
     */
    public void unsubscribeTopic(Integer topic_id) throws BadRequestException {

        User user = getUser();
        Topic topic = this.topicRepository.findById(topic_id).orElse(null);

        if (topic == null || user == null) {
            throw new NoSuchElementException();
        }

        boolean alreadySubscribe = user.getTopics().stream().anyMatch(o -> o.getId().equals(topic_id));
        if (!alreadySubscribe) {
            throw new BadRequestException();
        }

        user.setTopics(
                user.getTopics().stream().filter(topicList -> !topicList.getId().equals(topic_id))
                        .collect(Collectors.toList()));

        this.userRepository.save(user);
    }

    /**
     * Get authenticated user
     * 
     * @return user authenticated
     */
    public User getUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        LoginController.logger.trace("Me opération, email : " + email);
        User user = getUserByEmail(email);
        return user;
    }

    private List<GrantedAuthority> getGrantedAuthorities() {
        return new ArrayList<GrantedAuthority>();
    }

    private User dtoToEntity(UserDto userDto) {
        return modelMapper.map(userDto, User.class);
    }

    private UserDto entityToDto(User user) {
        return modelMapper.map(user, UserDto.class);
    }

    private List<TopicDto> entityToDtoList(List<Topic> user) {
        return modelMapper.map(user, new TypeToken<List<TopicDto>>() {
        }.getType());
    }

    private boolean isNotNullOrEmpty(String stringToControl) {
        return null != stringToControl && !stringToControl.isEmpty();
    }
}
