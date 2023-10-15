package com.example.serviceclient.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserDtoResponse {
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String id;
    private String contactAddress;
    private String emailAddress;
    private String password;
    private String token;
}
