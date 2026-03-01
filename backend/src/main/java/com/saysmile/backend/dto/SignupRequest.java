package com.saysmile.backend.dto;

import lombok.Data;

@Data
public class SignupRequest {
    private String username; // phone
    private String password;
    private String fullName;
}
