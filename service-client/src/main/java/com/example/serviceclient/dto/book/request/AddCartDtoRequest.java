package com.example.serviceclient.dto.book.request;

import com.example.serviceclient.dto.FileResponse;
import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class AddCartDtoRequest {
    @NotNull(message = "BookId cannot be null")
    @NotBlank(message = "BookId cannot be blank")
    private String bookId;

    @Positive(message = "Quantity cant be less than zero(0)")
    @Min(0)
    private Long quantity;
}
