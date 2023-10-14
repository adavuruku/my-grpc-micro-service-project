package com.example.serviceclient.dto.request;

import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Data;
import org.checkerframework.common.value.qual.MinLen;

@Data
@Builder
public class CreateUserDtoRequest {
    @NotEmpty(message = "First Name cannot be empty")
    @NotBlank(message = "First Name cannot be blank")
    @Pattern(regexp = "^[a-zA-Z]+(?:\\s+[a-zA-Z]+)*$", message = "Name must contain only alphabets along with white spaces.")
    private String firstName;

    @NotEmpty(message = "Last Name cannot be empty")
    @NotBlank(message = "Last Name cannot be blank")
    @Pattern(regexp = "^[a-zA-Z]+(?:\\s+[a-zA-Z]+)*$", message = "Name must contain only alphabets along with white spaces.")
    private String lastName;

    @NotEmpty(message = "Phone Number cannot be empty")
    @NotBlank(message = "Phone Number cannot be blank")
    private String phoneNumber;

    @NotEmpty(message = "Password cannot be empty")
    @NotBlank(message = "Password cannot be blank")
    @MinLen(6)
    private String password;

    @NotEmpty(message = "Contact address cannot be empty")
    @NotBlank(message = "Contact address cannot be blank")
    private String contactAddress;
    @NotEmpty(message = "Phone number cannot be empty")
    @NotBlank(message = "Phone number cannot be blank")
    @Email(message = "Invalid email address")
    private String emailAddress;
}
