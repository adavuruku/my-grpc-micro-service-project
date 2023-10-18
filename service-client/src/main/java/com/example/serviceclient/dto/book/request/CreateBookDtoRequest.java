package com.example.serviceclient.dto.book.request;

import com.example.serviceclient.dto.FileResponse;
import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;

import java.util.List;

@Data
@Builder
public class CreateBookDtoRequest {
    @NotNull(message = "Title cannot be null")
    @NotBlank(message = "Title cannot be blank")
    private String title;

    @NotNull(message = "Description cannot be null")
    @NotBlank(message = "Description cannot be blank")
    private String description;

    @NotEmpty(message = "Book must have atleast one authors")
    private List<String> authors;

    @Positive(message = "Quantity cant be less than zero(0)")
    @Min(0)
    private Long quantity;

//    private final boolean inStock = true;

    @NotNull(message = "ISBN cannot be null")
    @NotBlank(message = "ISBN cannot be blank")
    private String isbn;

    @NotEmpty(message = "Book must have atleast one image")
    private List<FileResponse> bookImages;
}
