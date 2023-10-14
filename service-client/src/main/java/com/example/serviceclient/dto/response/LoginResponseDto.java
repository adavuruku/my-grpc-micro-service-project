package com.example.serviceclient.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LoginResponseDto {
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String id;
    private String contactAddress;
    private String emailAddress;
    private String token;
    private String type = "Bearer";
}
