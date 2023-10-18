package com.example.bookservice.schema;

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

}
