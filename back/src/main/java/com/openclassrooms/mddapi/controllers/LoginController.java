package com.openclassrooms.mddapi.controllers;

import java.util.NoSuchElementException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
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
@RequestMapping("/api/auth")
@Tag(name = "Login")
public class LoginController {

    public static final Logger logger = LoggerFactory.getLogger(LoginController.class);
    @Autowired
    private JWTService jwtService;
    @Autowired
    private UserService userService;
    @Autowired
    private PasswordEncoder bcryptEncoder;

    /**
     * User login
     * 
     * @param loginDto
     * @return token
     */
    @PostMapping(value = "/login", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Authentication", description = "Authentication with email and password, return JWT")
    public ResponseEntity<LoginResponse> login(@RequestBody @Valid LoginDto loginDto) {
        logger.trace("Login user : " + loginDto);
        try {
            UserDto user = userService.userLogin(loginDto.getEmail(), loginDto.getPassword());
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
    @PostMapping(value = "/register", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Registration", description = "Register new user with email, username and password, return JWT")
    public ResponseEntity<LoginResponse> register(@RequestBody UserDto userDto) {
        logger.trace("Create user : " + userDto);
        try {
            userService.getUserByEmail(userDto.getEmail());
            logger.error("Create user error, user already exist : " + userDto);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        } catch (NoSuchElementException e) {
            userDto.setPassword(bcryptEncoder.encode(userDto.getPassword()));
            UserDto createdUser = userService.saveUser(userDto);
            logger.trace("createdUser : " + createdUser);
            String token = null;
            if (createdUser != null) {
                token = jwtService.generateToken(createdUser.getEmail());
            }
            return ResponseEntity.status(HttpStatus.CREATED).body(new LoginResponse(token));
        }
    }

    /**
     * Update user
     * 
     * @param userDto
     * @return token
     */
    @PutMapping(value = "/update", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Update", description = "Update user with email, username and password, return JWT")
    public ResponseEntity<LoginResponse> updateUser(@RequestBody UserDto userDto) {
        logger.trace("Update user : " + userDto);
        String token = null;
        try {
            if (isNotNullOrEmpty(userDto.getPassword()) || isNotNullOrEmpty(userDto.getEmail())
                    || isNotNullOrEmpty(userDto.getName())) {

                UserDto updatedUser = userService.updateUser(userDto);
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

    /**
     * Get logged user
     * 
     * @param authentication
     * @return current user
     */
    @GetMapping(value = "/me", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Me", description = "Get information about connected user")
    public ResponseEntity<User> getCurrentUser() {
        try {
            User user = userService.getUser();
            return new ResponseEntity<User>(user, HttpStatus.OK);
        } catch (NoSuchElementException e) {
            return new ResponseEntity<User>(HttpStatus.NOT_FOUND);
        }
    }

    private boolean isNotNullOrEmpty(String stringToControl) {
        return null != stringToControl && !stringToControl.isEmpty();
    }

}