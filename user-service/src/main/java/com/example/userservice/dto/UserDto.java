package com.example.userservice.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserDto {
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String password;
    private String emailAddress;
}
