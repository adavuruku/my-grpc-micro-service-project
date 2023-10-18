package com.example.serviceclient.dto.book.response;

import com.example.book_service.Book;
import com.example.serviceclient.dto.FileResponse;
import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;

import java.util.List;

@Data
@Builder
public class CreateBookDtoResponse {
    private String id;
    private String title;
    private String description;
    private List<String> authors;
    private Long quantity;
    private boolean inStock;
    private String bookSlug;
    private String isbn;
    private String createdBy;
    private String createdAt;
    private List<FileResponse> bookImages;

    public static CreateBookDtoResponse fromProtoToObject(Book book){
        return CreateBookDtoResponse.builder()
                .title(book.getTitle())
                .description(book.getDescription())
                .authors(book.getAuthorsList())
                .quantity(book.getQuantity())
                .bookSlug(book.getBookSlug())
                .isbn(book.getIsbn())
                .createdAt(book.getCreatedAt())
                .createdBy(book.getCreatedBy()).build();
    }
}
