package com.openclassrooms.mddapi.controllers;

import java.util.NoSuchElementException;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.openclassrooms.mddapi.controllers.dto.LoginDto;
import com.openclassrooms.mddapi.controllers.dto.UserDto;
import com.openclassrooms.mddapi.controllers.responses.LoginResponse;
import com.openclassrooms.mddapi.model.User;
import com.openclassrooms.mddapi.services.JWTService;
import com.openclassrooms.mddapi.services.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@Tag(name = "Login")
public class LoginController {

    private static final Logger logger = LoggerFactory.getLogger(LoginController.class);
    @Autowired
    private JWTService jwtService;
    @Autowired
    private UserService userService;
    @Autowired
    private PasswordEncoder bcryptEncoder;
    @Autowired
    private ModelMapper modelMapper;

    /**
     * User login
     * 
     * @param loginDto
     * @return token
     */
    @PostMapping(value = "api/auth/login", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Authentication", description = "Authentication with email and password, return JWT")
    public ResponseEntity<LoginResponse> userLogin(@RequestBody @Valid LoginDto loginDto) {
        logger.trace("Login user : " + loginDto);
        try {
            User user = userService.userLogin(loginDto.getEmail(), loginDto.getPassword());
            return ResponseEntity.status(HttpStatus.ACCEPTED)
                    .body(new LoginResponse(jwtService.generateToken(user.getEmail())));
        } catch (NoSuchElementException | BadCredentialsException ex) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    /**
     * Create user
     * 
     * @param userDto
     * @return token
     */
    @PostMapping(value = "api/auth/register", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Registration", description = "Register new user with email, username and password, return JWT")
    public ResponseEntity<LoginResponse> createUser(@RequestBody UserDto userDto) {
        logger.trace("Create user : " + userDto);
        try {
            userService.getUserByEmail(userDto.getEmail());
            logger.error("Create user error, user already exist : " + userDto);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        } catch (NoSuchElementException e) {
            userDto.setPassword(bcryptEncoder.encode(userDto.getPassword()));
            User createdUser = userService.saveUser(dtoToEntity(userDto));
            logger.trace("createdUser : " + createdUser);
            String token = null;
            if (createdUser != null) {
                token = jwtService.generateToken(createdUser.getEmail());
            }
            return ResponseEntity.status(HttpStatus.CREATED).body(new LoginResponse(token));
        }
    }

    /**
     * Create user
     * 
     * @param userDto
     * @return token
     */
    @PostMapping(value = "api/auth/update", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Update", description = "Update user with email, username and password, return JWT")
    public ResponseEntity<LoginResponse> updateUser(@RequestBody UserDto userDto) {
        logger.trace("Update user : " + userDto);
        String token = null;
        try {
            if (isNotNullOrEmpty(userDto.getPassword()) || isNotNullOrEmpty(userDto.getEmail())
                    || isNotNullOrEmpty(userDto.getName())) {
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
                User updatedUser = userService.saveUser(user);
                if (updatedUser != null) {

                    logger.trace("updatedUser : " + updatedUser);
                    token = jwtService.generateToken(updatedUser.getEmail());
                }
                return ResponseEntity.status(HttpStatus.OK).body(new LoginResponse(token));
            } else {
                logger.error("update user error : " + userDto);
                return ResponseEntity.status(HttpStatus.NOT_MODIFIED).build();
            }
        } catch (NoSuchElementException e) {
            logger.error("update user error : " + userDto);
            return ResponseEntity.status(HttpStatus.NOT_MODIFIED).build();
        }
    }

    private boolean isNotNullOrEmpty(String stringToControl) {
        return null != stringToControl && !stringToControl.isEmpty();
    }

    /**
     * Get logged user
     * 
     * @param authentication
     * @return current user
     */
    @GetMapping(value = "api/auth/me", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Me", description = "Get information about connected user")
    public ResponseEntity<User> getCurrentUser() {
        try {
            User user = getUser();
            return new ResponseEntity<User>(user, HttpStatus.OK);
        } catch (NoSuchElementException e) {
            return new ResponseEntity<User>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Get user by Id
     * 
     * @param id
     * @return user
     */
    @GetMapping(value = "api/user/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Get user by id", description = "Retrieve account information about user specified by his id")
    public ResponseEntity<User> getUserById(@PathVariable Integer id) {
        try {
            User user = userService.getUser(id);
            return new ResponseEntity<User>(user, HttpStatus.OK);
        } catch (NoSuchElementException e) {
            return new ResponseEntity<User>(HttpStatus.NOT_FOUND);
        }
    }

    private User getUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        logger.trace("Me opération, email : " + email);
        User user = userService.getUserByEmail(email);
        return user;
    }

    private User dtoToEntity(UserDto userDto) {
        return modelMapper.map(userDto, User.class);
    }
}