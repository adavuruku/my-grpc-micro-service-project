package com.example.bookservice.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BookDto {
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String password;
    private String emailAddress;
}
