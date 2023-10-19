package com.example.serviceclient.dto.book.request;

import com.example.serviceclient.dto.FileResponse;
import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class UpdateBookDtoRequest {

    @NotNull(message = "BookId cannot be null")
    @NotBlank(message = "BookId cannot be blank")
    private String id;

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

    @Min(value = 0, message = "Discount cant be less than zero(0)")
    @Digits(integer = 3, fraction = 1)
    private double discount;

    @Min(value = 0, message = "Price cant be less than zero(0)")
    @Digits(integer = 10, fraction = 1)
    private double price;

    @NotNull(message = "inStock cannot be null")
    private boolean inStock;

    @NotNull(message = "ISBN cannot be null")
    @NotBlank(message = "ISBN cannot be blank")
    private String isbn;

    @NotEmpty(message = "Book must have atleast one image")
    private List<FileResponse> bookImages;
}
