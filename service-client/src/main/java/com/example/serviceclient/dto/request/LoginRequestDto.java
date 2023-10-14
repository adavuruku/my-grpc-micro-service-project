package com.example.serviceclient.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;
import lombok.Data;
import org.checkerframework.common.value.qual.MinLen;

@Data
@Builder
public class LoginRequestDto {
    @NotEmpty(message = "Password cannot be empty")
    @NotBlank(message = "Password cannot be blank")
    private String password;

    @NotEmpty(message = "Phone number cannot be empty")
    @NotBlank(message = "Phone number cannot be blank")
    @Email(message = "Invalid email address")
    private String emailAddress;
}
