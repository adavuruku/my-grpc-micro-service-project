package com.example.serviceclient.dto.book.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UpdateCartDtoRequest {
    @NotNull(message = "CartId cannot be null")
    @NotBlank(message = "CartId cannot be blank")
    private String cartId;

    @Positive(message = "Quantity cant be less than zero(0)")
    @Min(0)
    private Long quantity;
}
