package com.example.serviceclient.dto.payment.request;

import com.example.serviceclient.dto.FileResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DeliveryAddressRequestDto {
    @NotNull
    @NotEmpty(message = "State cannot be empty")
    @NotBlank(message = "State cannot be blank")
    private String state;

    @NotNull
    @NotEmpty(message = "Local Government cannot be empty")
    @NotBlank(message = "Local Government cannot be blank")
    private String localGovernment;

    @NotNull
    @NotEmpty(message = "Location Address cannot be empty")
    @NotBlank(message = "Location Address cannot be blank")
    private String locationAddress;
}
