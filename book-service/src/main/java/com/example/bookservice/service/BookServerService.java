package com.example.bookservice.service;

import com.example.book_service.*;
import com.example.bookservice.exceptions.ErrorCode;
import com.example.bookservice.repo.BooksRepository;
import com.example.bookservice.schema.BookImageSchema;
import com.example.bookservice.schema.BookSchema;
import com.example.user_service.*;
import io.grpc.stub.StreamObserver;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.beanutils.BeanUtils;
import org.springframework.dao.DuplicateKeyException;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@AllArgsConstructor
@Log4j2
public class BookServerService extends BookServiceGrpc.BookServiceImplBase {
    private final BooksRepository booksRepository;

    @Override
    public void createBook(CreateBookRequest createBookRequest, StreamObserver<CreateBookResponse> createBookResponseStreamObserver) {
        try {
            Book bookRequest = createBookRequest.getBook();
            List<BookImageSchema> bookImageSchemaList = new ArrayList<>();

            for(BookImage bookImage : bookRequest.getBookImageList()){
                BookImageSchema bookImageSchema = BookImageSchema.builder().build();
                BeanUtils.copyProperties(bookImageSchema, bookImage);
                bookImageSchemaList.add(bookImageSchema);
            }
            BookSchema bookSchema = BookSchema.createSchema(bookRequest);
            bookSchema.setBookImages(bookImageSchemaList);

            BookSchema bookSchemaDto = booksRepository.save(bookSchema);
            Book newBook = Book.newBuilder()
                    .setInStock(bookSchemaDto.isInStock())
                    .setQuantity(bookSchemaDto.getQuantity())
                    .setIsbn(bookSchemaDto.getIsbn())
                    .setBookSlug(bookSchemaDto.getBookSlug())
                    .setCreatedBy(bookSchemaDto.getCreatedBy())
                    .setCreatedAt(bookSchemaDto.getCreatedAt())
                    .setDescription(bookSchemaDto.getDescription())
                    .addAllBookImage(bookRequest.getBookImageList())
                    .addAllAuthors(bookSchemaDto.getAuthors())
                    .setId(bookSchemaDto.getId())
                    .setTitle(bookSchemaDto.getTitle()).build();
            CreateBookResponse createBookResponse = CreateBookResponse.newBuilder().setBook(newBook).build();
            createBookResponseStreamObserver.onNext(createBookResponse);
            createBookResponseStreamObserver.onCompleted();
        } catch (DuplicateKeyException e) {
            throw new com.example.bookservice.exceptions.DuplicateKeyException(ErrorCode.USER_ALREADY_EXISTS,
                    ErrorCode.USER_ALREADY_EXISTS.getMessage(), Map.of( "message", "Book with title: "+createBookRequest.getBook().getTitle()+" already exist"));
        } catch (InvocationTargetException | IllegalAccessException ex) {
            throw new RuntimeException(ex);
        } catch (Exception e) {
            throw e;
        }
    }
}
