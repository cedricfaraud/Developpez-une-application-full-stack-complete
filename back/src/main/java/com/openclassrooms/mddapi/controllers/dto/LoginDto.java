package com.openclassrooms.mddapi.controllers.dto;

import lombok.Data;

@Data
public class LoginDto {
    private String email;

    private String password;
}
