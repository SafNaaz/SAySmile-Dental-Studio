package com.saysmile.backend.dto;

import lombok.Data;

@Data
public class LoginRequest {
    private String username;
    private String password;
    // userType could be "STAFF" or "PATIENT" to know which auth strategy to use
    private String userType;
}
