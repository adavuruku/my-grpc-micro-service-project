package com.example.serviceclient.service.book;

import com.example.book_service.*;
import com.example.serviceclient.dto.FileResponse;
import com.example.serviceclient.dto.book.request.CreateBookDtoRequest;
import com.example.serviceclient.dto.book.response.CreateBookDtoResponse;
import com.example.serviceclient.dto.user.request.CreateUserDtoRequest;
import com.example.serviceclient.dto.user.response.CreateUserDtoResponse;
import com.example.serviceclient.exceptions.ServiceExceptionMapper;
import com.example.user_service.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.slugify.Slugify;
import io.grpc.StatusRuntimeException;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.beanutils.BeanUtils;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;


@Log4j2
public class BookService {
    private final BookServiceGrpc.BookServiceBlockingStub synchronousClient;

    private final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    public BookService(BookServiceGrpc.BookServiceBlockingStub synchronousClient){
        this.synchronousClient = synchronousClient;
    }

    public CreateBookDtoResponse createBook(CreateBookDtoRequest createBookDtoRequest, String userName) {
        try {
            List<BookImage> bookImages = new ArrayList<>();
            for(FileResponse fileResponse : createBookDtoRequest.getBookImages()){
                BookImage bookImage = BookImage.newBuilder()
                        .setCreatedAt(fileResponse.getCreatedAt())
                        .setSecureUrl(fileResponse.getSecureUrl())
                        .setHeight(fileResponse.getHeight())
                        .setBytes(fileResponse.getBytes())
                        .setFormat(fileResponse.getFormat())
                        .setResourceType(fileResponse.getResourceType())
                        .setWidth(fileResponse.getWidth())
                        .setUrl(fileResponse.getUrl())
                        .build();
                bookImages.add(bookImage);
            }

            // create user message (protobuf)
            Book book = Book.newBuilder()
                    .setTitle(createBookDtoRequest.getTitle())
                    .addAllBookImage(bookImages)
                    .addAllAuthors(createBookDtoRequest.getAuthors())
                    .setCreatedAt(new Date().toString())
                    .setCreatedBy(userName)
                    .setBookSlug(createSlug(createBookDtoRequest.getTitle()))
                    .setDescription(createBookDtoRequest.getDescription())
                    .setIsbn(createBookDtoRequest.getIsbn())
                    .setQuantity(createBookDtoRequest.getQuantity())
                    .setInStock(true).build();

            // create the request (protobuf) to pass to server
            CreateBookRequest createBookRequest = CreateBookRequest.newBuilder().setBook(book).build();
            CreateBookResponse createBookResponse = synchronousClient.createBook(createBookRequest);

            book = createBookResponse.getBook();

            List<FileResponse> fileResponseList = new ArrayList<>();
            for(BookImage bookImage : book.getBookImageList()){
                FileResponse fileResponse = FileResponse.builder()
                        .createdAt(bookImage.getCreatedAt())
                        .secureUrl(bookImage.getSecureUrl())
                        .height(bookImage.getHeight())
                        .bytes(bookImage.getBytes())
                        .format(bookImage.getFormat())
                        .resourceType(bookImage.getResourceType())
                        .width(bookImage.getWidth())
                        .url(bookImage.getUrl())
                        .build();
                fileResponseList.add(fileResponse);
            }

            CreateBookDtoResponse createBookDtoResponse = CreateBookDtoResponse.builder()
                    .title(book.getTitle())
                    .description(book.getDescription())
                    .authors(book.getAuthorsList())
                    .bookImages(fileResponseList)
                    .quantity(book.getQuantity())
                    .bookSlug(book.getBookSlug())
                    .isbn(book.getIsbn())
                    .createdAt(book.getCreatedAt())
                    .createdBy(book.getCreatedBy()).build();

//            BeanUtils.copyProperties(createBookDtoResponse, createBookResponse.getBook());
            return createBookDtoResponse;
        } catch (StatusRuntimeException error) {
            var status = io.grpc.protobuf.StatusProto.fromThrowable(error);
            log.error("Error reason {}", status.getMessage());
            throw ServiceExceptionMapper.map(error);
        }
//        catch (IllegalAccessException | InvocationTargetException error) {
//            throw new RuntimeException(error);
//        }
    }

    public String createSlug(String title){
        Slugify slg = Slugify.builder().build();
        String slugTitle = slg.slugify(title + "-"+ UUID.randomUUID().toString().replace("-",""));
        return slugTitle;
    }
}
