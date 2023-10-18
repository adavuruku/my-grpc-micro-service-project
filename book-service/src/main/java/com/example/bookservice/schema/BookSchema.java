package com.example.bookservice.schema;

import com.example.book_service.Book;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@Document("books")
@Builder
//@NoArgsConstructor
public class BookSchema {
    @Id
    private String id;
    private String title;
    private String description;
    private List<String> authors;
    private Long quantity;
    private boolean inStock;
    private String createdAt;
    private String createdBy;
    @Indexed(unique = true)
    private String bookSlug;
    private String isbn;
    private List<BookImageSchema> bookImages;

    public static BookSchema createSchema(Book bookRequest){
        return BookSchema.builder()
                .title(bookRequest.getTitle())
                .description(bookRequest.getDescription())
                .authors(bookRequest.getAuthorsList())
                .quantity(bookRequest.getQuantity())
                .bookSlug(bookRequest.getBookSlug())
                .isbn(bookRequest.getIsbn())
                .createdAt(bookRequest.getCreatedAt())
                .createdBy(bookRequest.getCreatedBy())
                .build();
    }
}
