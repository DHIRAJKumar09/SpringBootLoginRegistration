package com.tcs.registration_login.userauthentication.dto;


import lombok.Data;

@Data
public class LoginRequest {
    private String username;
    private String password;
}
