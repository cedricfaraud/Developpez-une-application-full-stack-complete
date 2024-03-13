package com.openclassrooms.mddapi.controllers.dto;

import lombok.Data;

@Data
public class UserDto {

    private String name;

    private String email;

    private String password;
    /*
     * public User userDtoToUser() {
     * User user = new User();
     * user.setName(getName());
     * user.setEmail(getEmail());
     * user.setPassword(getPassword());
     * return user;
     * }
     */
}
