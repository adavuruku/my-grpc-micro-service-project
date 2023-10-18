package com.example.serviceclient.dto.user.request;

import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UpdatePasswordDtoRequest {
    @NotNull
    @NotEmpty(message = "Password cannot be empty")
    @NotBlank(message = "Password cannot be blank")
    @Min(6)
    private String currentPassword;

    @NotNull
    @NotEmpty(message = "Password cannot be empty")
    @NotBlank(message = "Password cannot be blank")
    @Min(6)
    private String newPassword;
}
