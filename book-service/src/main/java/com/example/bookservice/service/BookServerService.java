package com.example.bookservice.service;

import com.example.book_service.*;
import com.example.bookservice.exceptions.ErrorCode;
import com.example.bookservice.exceptions.ResourceNotFoundException;
import com.example.bookservice.repo.BooksRepository;
import com.example.bookservice.repo.CartsRepository;
import com.example.bookservice.schema.BookImageSchema;
import com.example.bookservice.schema.BookSchema;
import com.example.bookservice.schema.CartSchema;
import com.example.user_service.*;
import io.grpc.stub.StreamObserver;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.beanutils.BeanUtils;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.lang.reflect.InvocationTargetException;
import java.util.*;

@AllArgsConstructor
@Log4j2
public class BookServerService extends BookServiceGrpc.BookServiceImplBase {
    private final BooksRepository booksRepository;
    private final CartsRepository cartsRepository;

    @Override
    public void createBook(CreateBookRequest createBookRequest, StreamObserver<CreateBookResponse> createBookResponseStreamObserver) {
        try {
            Book bookRequest = createBookRequest.getBook();
            //1. Convert all proto schemas or java pojo and save
            List<BookImageSchema> bookImageSchemaList = BookImageSchema.createListOfBookingImageSchemaFromBookImageProto(bookRequest.getBookImageList());
            BookSchema bookSchema = BookSchema.convertBookProtoToBookSchema(bookRequest, bookImageSchemaList);
            BookSchema bookSchemaDto = booksRepository.save(bookSchema);

            //1. Convert all pojo or schemas to proto file and transmit to client
            Book newBook = BookSchema.convertBookSchemaToBookProto(bookSchemaDto);
            CreateBookResponse createBookResponse = CreateBookResponse.newBuilder().setBook(newBook).build();

            createBookResponseStreamObserver.onNext(createBookResponse);
            createBookResponseStreamObserver.onCompleted();

        } catch (Exception e) {
            throw e;
        }
    }

    public void listBooks(ListBookRequest listBookRequest, StreamObserver<ListBookResponse> listBookResponseStreamObserver) {
        try{
            //1. Paginate and get records from DB
            Sort sort = Sort.by(Sort.Direction.DESC, "createdAt");
            Pageable paging = PageRequest.of(listBookRequest.getPage(), listBookRequest.getPerPage(), sort);
            Page<BookSchema> pageBookSchema = booksRepository.findByisDeleted(false, paging);
            List<BookSchema> bookSchemaList = pageBookSchema.getContent();

            Pagination pagination = Pagination.newBuilder().setPerPage(pageBookSchema.getSize())
                    .setCurrentPage(pageBookSchema.getNumber()).setTotalPage(pageBookSchema.getTotalPages())
                    .setHasNext(pageBookSchema.hasNext()).setHasPrevious(pageBookSchema.hasPrevious())
                    .setTotalItem(pageBookSchema.getTotalElements()).build();

            //2. Convert schemas to proto for data transport
            List<Book> bookList = BookSchema.convertListOfBookSchemaToListOfBookProto(bookSchemaList);

            ListBookResponse listBookResponse = ListBookResponse.newBuilder()
                    .addAllBook(bookList)
                    .setPagination(pagination).build();

            listBookResponseStreamObserver.onNext(listBookResponse);
            listBookResponseStreamObserver.onCompleted();

        }catch (Exception e) {
            throw e;
        }
    }

    public void openBook(OpenBookRequest openBookRequest, StreamObserver<Book> bookStreamObserver) {
        try{
            Optional<BookSchema> bookSchema = booksRepository.findByIdOrBookSlug(
                    openBookRequest.getSearchCriteria(),openBookRequest.getSearchCriteria()
            );
            if(bookSchema.isEmpty()){
                throw new ResourceNotFoundException("Resource not found.",  Map.of("Book with Id or slug: ", openBookRequest.getSearchCriteria(), "message", "Resource Not Found"));
            }
            BookSchema bookSchemaItem = bookSchema.get();
            //2. Convert schemas to proto for data transport
            Book newBook = BookSchema.convertBookSchemaToBookProto(bookSchemaItem);
            bookStreamObserver.onNext(newBook);
            bookStreamObserver.onCompleted();

        }catch (Exception e) {
            log.info("{}", e);
            throw e;
        }
    }

    public void deleteBook(OpenBookRequest openBookRequest, StreamObserver<DeleteBookResponse> deleteBookResponseStreamObserver) {
        try{

            Optional<BookSchema> bookSchema = booksRepository.findByIdOrBookSlug(
                    openBookRequest.getSearchCriteria(), openBookRequest.getSearchCriteria()
            );
            if(bookSchema.isEmpty()){
                throw new ResourceNotFoundException("Resource not found.",  Map.of("Book with Id or slug: ", openBookRequest.getSearchCriteria(), "message", "Resource Not Found"));
            }
            BookSchema bookSchemaItem = bookSchema.get();
            bookSchemaItem.setDeleted(true);
            booksRepository.save(bookSchemaItem);
            //2. Convert schemas to proto for data transport
            DeleteBookResponse deleteRecord = DeleteBookResponse.newBuilder()
                    .setId(bookSchemaItem.getId()).setStatusMessage("Book successfully Deleted.").build();
            deleteBookResponseStreamObserver.onNext(deleteRecord);
            deleteBookResponseStreamObserver.onCompleted();

        }catch (Exception e) {
            throw e;
        }
    }

    /**
     * CARTS BEGINS
     * @param addCartRequest
     * @param cartStreamObserver
     */

    public void addCart(AddCartRequest addCartRequest, StreamObserver<Cart> cartStreamObserver) {
        try{

            Optional<BookSchema> bookSchema = booksRepository.findById(addCartRequest.getBookId());
            if(bookSchema.isEmpty()){
                throw new ResourceNotFoundException("Resource not found.",  Map.of("Book with Id or slug: ", addCartRequest.getBookId(), "message", "Resource Not Found"));
            }
            BookSchema bookSchemaItem = bookSchema.get();
            CartSchema cartSchema = CartSchema.builder()
                    .book(bookSchemaItem).createdAt(new Date().toString()).createdBy(addCartRequest.getUserName()).quantity(addCartRequest.getQuantity()).build();
            CartSchema cartSchemaData = cartsRepository.save(cartSchema);

            //2. Convert schemas to proto for data transport
            Book bookAdd = BookSchema.convertBookSchemaToBookProto(cartSchemaData.getBook());
            Cart deleteRecord = Cart.newBuilder()
                    .setId(cartSchemaData.getId()).setBook(bookAdd)
                    .setCreatedAt(cartSchemaData.getCreatedAt()).setCreatedBy(cartSchemaData.getCreatedBy())
                    .setQuantity(cartSchemaData.getQuantity()).build();
            cartStreamObserver.onNext(deleteRecord);
            cartStreamObserver.onCompleted();

        }catch (Exception e) {
            throw e;
        }
    }

    public void deleteCart(DeleteCartRequest deleteCartRequest, StreamObserver<DeleteCartResponse> deleteCartResponseStreamObserver) {
        try{

            Optional<CartSchema> cartSchema = cartsRepository.findByIdAndCreatedBy(deleteCartRequest.getCartId(), deleteCartRequest.getUserName());
            if(cartSchema.isEmpty()){
                throw new ResourceNotFoundException("Resource not found.",  Map.of("Cart Item with Id: ", deleteCartRequest.getCartId(), "message", "Resource Not Found"));
            }
            CartSchema cartSchemaItem = cartSchema.get();
            cartsRepository.delete(cartSchemaItem);
            //2. Convert schemas to proto for data transport
            DeleteCartResponse deleteRecord = DeleteCartResponse.newBuilder()
                    .setId(cartSchemaItem.getId()).setStatusMessage("Cart Item successfully Removed.").build();
            deleteCartResponseStreamObserver.onNext(deleteRecord);
            deleteCartResponseStreamObserver.onCompleted();
        }catch (Exception e) {
            throw e;
        }
    }

    public void listCarts(ListCartRequest listCartRequest, StreamObserver<ListCartResponse> listCartResponseStreamObserver) {
        try{
            //1. Paginate and get records from DB
            Sort sort = Sort.by(Sort.Direction.DESC, "createdAt");
            Pageable paging = PageRequest.of(listCartRequest.getPage(), listCartRequest.getPerPage(), sort);
            Page<CartSchema> pageCartSchema = cartsRepository.findByCreatedBy(listCartRequest.getUserName(), paging);
            List<CartSchema> cartSchemaList = pageCartSchema.getContent();

            log.info("cartSchemaList {}",cartSchemaList.get(0).getBook());

            Pagination pagination = Pagination.newBuilder().setPerPage(pageCartSchema.getSize())
                    .setCurrentPage(pageCartSchema.getNumber()).setTotalPage(pageCartSchema.getTotalPages())
                    .setHasNext(pageCartSchema.hasNext()).setHasPrevious(pageCartSchema.hasPrevious())
                    .setTotalItem(pageCartSchema.getTotalElements()).build();

            //2. Convert schemas to proto for data transport
            List<Cart> cartList = CartSchema.convertListOfCartSchemaToListOfCartProto(cartSchemaList);

            ListCartResponse listCartResponse = ListCartResponse.newBuilder()
                    .addAllCarts(cartList)
                    .setPagination(pagination).build();

            listCartResponseStreamObserver.onNext(listCartResponse);
            listCartResponseStreamObserver.onCompleted();

        }catch (Exception e) {
            throw e;
        }
    }
}
