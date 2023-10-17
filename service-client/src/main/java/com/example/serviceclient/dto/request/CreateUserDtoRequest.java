package com.example.serviceclient.dto.request;

import com.example.serviceclient.dto.response.FileResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CreateUserDtoRequest {
    @NotNull
    @NotEmpty(message = "First Name cannot be empty")
    @NotBlank(message = "First Name cannot be blank")
    @Pattern(regexp = "^[a-zA-Z]+(?:\\s+[a-zA-Z]+)*$", message = "Name must contain only alphabets along with white spaces.")
    private String firstName;

    @NotNull
    @NotEmpty(message = "Last Name cannot be empty")
    @NotBlank(message = "Last Name cannot be blank")
    @Pattern(regexp = "^[a-zA-Z]+(?:\\s+[a-zA-Z]+)*$", message = "Name must contain only alphabets along with white spaces.")
    private String lastName;

    @NotNull
    @NotEmpty(message = "Phone Number cannot be empty")
    @NotBlank(message = "Phone Number cannot be blank")
    private String phoneNumber;

    @NotNull
    @NotEmpty(message = "Password cannot be empty")
    @NotBlank(message = "Password cannot be blank")
    @Min(6)
    private String password;

    @NotNull
    @Valid
    private FileResponse profileImage;

    @NotNull
    @NotEmpty(message = "Contact address cannot be empty")
    @NotBlank(message = "Contact address cannot be blank")
    private String contactAddress;

    @NotNull
    @NotEmpty(message = "Phone number cannot be empty")
    @NotBlank(message = "Phone number cannot be blank")
    @Email(message = "Invalid email address")
    private String emailAddress;
}
