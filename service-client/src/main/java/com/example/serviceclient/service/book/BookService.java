package com.example.serviceclient.service.book;

import com.example.book_service.*;
import com.example.serviceclient.dto.FileResponse;
import com.example.serviceclient.dto.book.request.AddCartDtoRequest;
import com.example.serviceclient.dto.book.request.CreateBookDtoRequest;
import com.example.serviceclient.dto.book.response.*;
import com.example.serviceclient.exceptions.ServiceExceptionMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.slugify.Slugify;
import io.grpc.StatusRuntimeException;
import lombok.extern.log4j.Log4j2;

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
            List<BookImage> bookImages = FileResponse
                    .fromListOfFileResponsePojoToListOfFileResponseProto(createBookDtoRequest.getBookImages());

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

            //process response from server
            book = createBookResponse.getBook();
            CreateBookDtoResponse createBookDtoResponse = CreateBookDtoResponse.fromProtoToObject(book);

            return createBookDtoResponse;
        } catch (StatusRuntimeException error) {
            var status = io.grpc.protobuf.StatusProto.fromThrowable(error);
            log.error("Error reason {}", status.getMessage());
            throw ServiceExceptionMapper.map(error);
        }
    }

    public ListBookDtoResponse getAllBooks(int page, int perPage) {
        try {
            //1. Create request and access server
            ListBookRequest listBookRequest = ListBookRequest.newBuilder()
                    .setPage(page).setPerPage(perPage).build();

            ListBookResponse createBookResponse = synchronousClient.listBooks(listBookRequest);

            //2. convert protobuf to pojo
            List<CreateBookDtoResponse> createBookDtoResponseList = CreateBookDtoResponse
                    .fromListOfBookResponseProtoToPojo(createBookResponse.getBookList());

            Pagination pagination = createBookResponse.getPagination();
            BookPagination bookPagination = BookPagination.builder()
                    .currentPage(pagination.getCurrentPage())
                    .hasNext(pagination.getHasNext()).hasPrevious(pagination.getHasPrevious())
                    .perPage(pagination.getPerPage()).totalPage(pagination.getTotalPage()).totalItem(pagination.getTotalItem()).build();

            ListBookDtoResponse listBookDtoResponse = ListBookDtoResponse.builder()
                    .books(createBookDtoResponseList).pagination(bookPagination).build();
            return listBookDtoResponse;
        } catch (StatusRuntimeException error) {
            var status = io.grpc.protobuf.StatusProto.fromThrowable(error);
            log.error("Error reason {}", status.getMessage());
            throw ServiceExceptionMapper.map(error);
        }
    }

    public CreateBookDtoResponse openABook(String searchCriteria) {
        try {
            OpenBookRequest openBookRequest = OpenBookRequest.newBuilder().setSearchCriteria(searchCriteria).build();
            Book responseBook = synchronousClient.openBook(openBookRequest);
            //2. convert protobuf to pojo
            CreateBookDtoResponse createBookDtoResponse = CreateBookDtoResponse
                    .fromProtoToObject(responseBook);
            return createBookDtoResponse;
        } catch (StatusRuntimeException error) {
            log.error("Error reason {} ", error.getMessage());
            throw ServiceExceptionMapper.map(error);
        }
    }

    public DeleteDtoResponse deleteABook(String id) {
        try {
            OpenBookRequest openBookRequest = OpenBookRequest.newBuilder().setSearchCriteria(id).build();
            DeleteBookResponse responseBook = synchronousClient.deleteBook(openBookRequest);
            //2. convert protobuf to pojo
            DeleteDtoResponse deleteBookDtoResponse = DeleteDtoResponse.builder()
                    .id(responseBook.getId()).statusMessage(responseBook.getStatusMessage()).build();
            return deleteBookDtoResponse;
        } catch (StatusRuntimeException error) {
            log.error("Error reason {} ", error.getMessage());
            throw ServiceExceptionMapper.map(error);
        }
    }

    /**
     * CART SERVICE BEGIN
     * **/

    public AddCartDtoResponse addCart(AddCartDtoRequest addCartDtoRequest, String userName) {
        try {
            // create the request (protobuf) to pass to server
            AddCartRequest addCartRequest = AddCartRequest.newBuilder()
                    .setBookId(addCartDtoRequest.getBookId()).setQuantity(addCartDtoRequest.getQuantity())
                    .setUserName(userName).build();
            Cart newCart = synchronousClient.addCart(addCartRequest);
            AddCartDtoResponse addCartDtoResponse = AddCartDtoResponse.fromProtoToObject(newCart);

            return addCartDtoResponse;
        } catch (StatusRuntimeException error) {
            var status = io.grpc.protobuf.StatusProto.fromThrowable(error);
            log.error("Error reason {}", status.getMessage());
            throw ServiceExceptionMapper.map(error);
        }
    }

    public DeleteDtoResponse deleteACart(String id, String userName) {
        try {
            DeleteCartRequest deleteCartRequest = DeleteCartRequest.newBuilder().setCartId(id)
                    .setUserName(userName).build();
            DeleteCartResponse responseCart = synchronousClient.deleteCart(deleteCartRequest);
            //2. convert protobuf to pojo
            DeleteDtoResponse deleteDtoResponse = DeleteDtoResponse.builder()
                    .id(responseCart.getId()).statusMessage(responseCart.getStatusMessage()).build();
            return deleteDtoResponse;
        } catch (StatusRuntimeException error) {
            log.error("Error reason {} ", error.getMessage());
            throw ServiceExceptionMapper.map(error);
        }
    }

    public ListCartDtoResponse getAllCart(int page, int perPage, String userName) {
        try {
            //1. Create request and access server
            ListCartRequest listCartRequest = ListCartRequest.newBuilder()
                    .setPage(page).setPerPage(perPage).setUserName(userName).build();

            ListCartResponse createCartResponse = synchronousClient.listCarts(listCartRequest);

            //2. convert protobuf to pojo
            List<AddCartDtoResponse> addCartDtoResponseList = AddCartDtoResponse
                    .fromListOfCartResponseProtoToPojo(createCartResponse.getCartsList());

            Pagination pagination = createCartResponse.getPagination();
            BookPagination bookPagination = BookPagination.builder()
                    .currentPage(pagination.getCurrentPage())
                    .hasNext(pagination.getHasNext()).hasPrevious(pagination.getHasPrevious())
                    .perPage(pagination.getPerPage()).totalPage(pagination.getTotalPage()).totalItem(pagination.getTotalItem()).build();

            ListCartDtoResponse listCartDtoResponse = ListCartDtoResponse.builder()
                    .carts(addCartDtoResponseList).pagination(bookPagination).build();
            return listCartDtoResponse;
        } catch (StatusRuntimeException error) {
            var status = io.grpc.protobuf.StatusProto.fromThrowable(error);
            log.error("Error reason {}", status.getMessage());
            throw ServiceExceptionMapper.map(error);
        }
    }

    public String createSlug(String title){
        Slugify slg = Slugify.builder().build();
        String slugTitle = slg.slugify(title + "-"+ UUID.randomUUID().toString().replace("-",""));
        return slugTitle;
    }
}
