package com.example.bookservice.schema;

import com.example.book_service.Book;
import com.example.book_service.BookImage;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
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
    private boolean isDeleted;
    private String createdAt;
    private String createdBy;
    @Indexed(unique = true)
    private String bookSlug;
    private String isbn;
    private List<BookImageSchema> bookImages;

    public static BookSchema convertBookProtoToBookSchema(Book bookRequest, List<BookImageSchema> bookImageSchemaList){
        return BookSchema.builder()
                .title(bookRequest.getTitle())
                .description(bookRequest.getDescription())
                .authors(bookRequest.getAuthorsList())
                .quantity(bookRequest.getQuantity())
                .bookSlug(bookRequest.getBookSlug())
                .isbn(bookRequest.getIsbn())
                .bookImages(bookImageSchemaList)
                .createdAt(bookRequest.getCreatedAt())
                .createdBy(bookRequest.getCreatedBy())
                .isDeleted(false)
                .build();
    }

    public static Book convertBookSchemaToBookProto(BookSchema bookSchemaDto){
        List<BookImage> bookImageList = BookImageSchema
                .createListOfBookImageProtoFromBookingImageSchema(bookSchemaDto.getBookImages());
        return Book.newBuilder()
                .setInStock(bookSchemaDto.isInStock())
                .setQuantity(bookSchemaDto.getQuantity())
                .setIsbn(bookSchemaDto.getIsbn())
                .setBookSlug(bookSchemaDto.getBookSlug())
                .setCreatedBy(bookSchemaDto.getCreatedBy())
                .setCreatedAt(bookSchemaDto.getCreatedAt())
                .setDescription(bookSchemaDto.getDescription())
                .addAllBookImage(bookImageList)
                .addAllAuthors(bookSchemaDto.getAuthors())
                .setId(bookSchemaDto.getId())
                .setTitle(bookSchemaDto.getTitle()).build();

    }

    public static List<Book> convertListOfBookSchemaToListOfBookProto(List<BookSchema> bookSchemaList){

        List<Book> bookList = new ArrayList<>();
        for(BookSchema bookSchemaDto : bookSchemaList){

            List<BookImage> bookImageList = BookImageSchema
                    .createListOfBookImageProtoFromBookingImageSchema(bookSchemaDto.getBookImages());

            Book book = Book.newBuilder()
                    .setId(bookSchemaDto.getId())
                    .setInStock(bookSchemaDto.isInStock())
                    .setQuantity(bookSchemaDto.getQuantity())
                    .setIsbn(bookSchemaDto.getIsbn())
                    .setBookSlug(bookSchemaDto.getBookSlug())
                    .setCreatedBy(bookSchemaDto.getCreatedBy())
                    .setCreatedAt(bookSchemaDto.getCreatedAt())
                    .setDescription(bookSchemaDto.getDescription())
                    .addAllBookImage(bookImageList)
                    .addAllAuthors(bookSchemaDto.getAuthors())
                    .setId(bookSchemaDto.getId())
                    .setTitle(bookSchemaDto.getTitle()).build();
            bookList.add(book);
        }

        return bookList;
    }
}
