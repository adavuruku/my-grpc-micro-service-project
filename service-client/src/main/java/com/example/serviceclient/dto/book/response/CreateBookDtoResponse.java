package com.example.serviceclient.dto.book.response;

import com.example.book_service.Book;
import com.example.serviceclient.dto.FileResponse;
import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@Slf4j
public class CreateBookDtoResponse {
    private String id;
    private String title;
    private String description;
    private List<String> authors;
    private Long quantity;
    private double discount;
    private double price;
    private boolean inStock;
    private String bookSlug;
    private String isbn;
    private String createdBy;
    private String createdAt;
    private List<FileResponse> bookImages;

    public static CreateBookDtoResponse fromProtoToObject(Book book){
        List<FileResponse> fileResponseList = FileResponse
                .fromListOfFileResponseProtoToListOfFileResponsePojo(book.getBookImageList());
        return CreateBookDtoResponse.builder()
                .id(book.getId())
                .title(book.getTitle())
                .description(book.getDescription())
                .authors(book.getAuthorsList())
                .discount(book.getDiscount())
                .price(book.getPrice())
                .bookImages(fileResponseList)
                .quantity(book.getQuantity())
                .bookSlug(book.getBookSlug())
                .isbn(book.getIsbn())
                .inStock(book.getInStock())
                .createdAt(book.getCreatedAt())
                .createdBy(book.getCreatedBy()).build();
    }

    public static List<CreateBookDtoResponse> fromListOfBookResponseProtoToPojo(List<Book> bookList){
        List<CreateBookDtoResponse> listOfBookResponse = new ArrayList<>();

        for(Book book : bookList){
            List<FileResponse> fileResponseList = FileResponse
                    .fromListOfFileResponseProtoToListOfFileResponsePojo(book.getBookImageList());
            CreateBookDtoResponse createBookDtoResponse = CreateBookDtoResponse.builder()
                    .id(book.getId())
                    .inStock(book.getInStock())
                    .title(book.getTitle())
                    .description(book.getDescription())
                    .discount(book.getDiscount())
                    .price(book.getPrice())
                    .authors(book.getAuthorsList())
                    .quantity(book.getQuantity())
                    .bookSlug(book.getBookSlug())
                    .isbn(book.getIsbn())
                    .bookImages(fileResponseList)
                    .createdAt(book.getCreatedAt())
                    .createdBy(book.getCreatedBy()).build();

            listOfBookResponse.add(createBookDtoResponse);

        }
        return listOfBookResponse;
    }
}
