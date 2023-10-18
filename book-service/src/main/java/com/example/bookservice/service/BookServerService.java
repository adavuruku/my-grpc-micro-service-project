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
            //1. Convert all proto schemas or java pojo and save
            List<BookImageSchema> bookImageSchemaList = BookImageSchema.createListOfBookingImageSchemaFromBookImageProto(bookRequest.getBookImageList());
            BookSchema bookSchema = BookSchema.convertBookProtoToBookSchema(bookRequest, bookImageSchemaList);
            BookSchema bookSchemaDto = booksRepository.save(bookSchema);

            //1. Convert all pojo or schemas to proto file and transmit to client
            List<BookImage> bookImageList = BookImageSchema.createListOfBookImageProtoFromBookingImageSchema(bookSchemaDto.getBookImages());
            Book newBook = BookSchema.convertBookSchemaToBookProto(bookSchemaDto, bookImageList);
            CreateBookResponse createBookResponse = CreateBookResponse.newBuilder().setBook(newBook).build();

            createBookResponseStreamObserver.onNext(createBookResponse);
            createBookResponseStreamObserver.onCompleted();

        } catch (DuplicateKeyException e) {
            throw new com.example.bookservice.exceptions.DuplicateKeyException(ErrorCode.USER_ALREADY_EXISTS,
                    ErrorCode.USER_ALREADY_EXISTS.getMessage(), Map.of( "message", "Book with title: "+createBookRequest.getBook().getTitle()+" already exist"));
        } catch (Exception e) {
            throw e;
        }
    }
}
