package com.example.serviceclient.controller.book;

import com.example.serviceclient.dto.book.request.AddCartDtoRequest;
import com.example.serviceclient.dto.book.request.CreateBookDtoRequest;
import com.example.serviceclient.dto.book.response.*;
import com.example.serviceclient.service.book.BookService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Positive;
import jakarta.websocket.server.PathParam;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/api/v1/books")
@AllArgsConstructor
@Log4j2
@Validated
public class BookController {

    @Autowired
    BookService bookClientService;

    @PostMapping(value = "/add", consumes = {MediaType.APPLICATION_JSON_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<Object> createBook(@RequestBody @Valid CreateBookDtoRequest createBookDtoRequest, Principal principal){
        CreateBookDtoResponse response = bookClientService.createBook(createBookDtoRequest, principal.getName());
        return ResponseEntity.ok(response);
    }

    @GetMapping(value="/all", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<ListBookDtoResponse> listBooks(@RequestParam("page") @Min(0) int page,
                                                         @RequestParam("perPage") @Positive int perPage){
        ListBookDtoResponse response = bookClientService.getAllBooks(page , perPage);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping(value="/delete/{searchCriteria}", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<DeleteDtoResponse> deleteBook(@PathVariable("searchCriteria") @NotBlank @NotEmpty String searchCriteria, Principal principal){
        DeleteDtoResponse response = bookClientService.deleteABook(searchCriteria);
        return ResponseEntity.ok(response);
    }

    @GetMapping(value="/open/{searchCriteria}", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<CreateBookDtoResponse> openBook(@PathVariable("searchCriteria") @NotBlank @NotEmpty String searchCriteria){
        CreateBookDtoResponse response = bookClientService.openABook(searchCriteria);
        return ResponseEntity.ok(response);
    }

    @GetMapping(value="/carts/all", produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<ListCartDtoResponse> listBooks(@RequestParam("page") @Min(0) int page,
                                                         @RequestParam("perPage") @Positive int perPage, Principal principal){
        ListCartDtoResponse response = bookClientService.getAllCart(page , perPage, principal.getName());
        return ResponseEntity.ok(response);
    }

    @PostMapping(value="/carts/add", consumes = {MediaType.APPLICATION_JSON_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<AddCartDtoResponse>   addCards(@RequestBody @Valid AddCartDtoRequest addCartDtoRequest, Principal principal){
        AddCartDtoResponse response = bookClientService.addCart(addCartDtoRequest, principal.getName());
        return ResponseEntity.ok(response);
    }

    @DeleteMapping(value="/carts/delete/{cartId}", consumes = {MediaType.APPLICATION_JSON_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<DeleteDtoResponse>   addCards(@PathVariable("cartId") @NotBlank @NotEmpty String cartId, Principal principal){
        DeleteDtoResponse response = bookClientService.deleteACart(cartId, principal.getName());
        return ResponseEntity.ok(response);
    }

    /***
     * To Do
     * Update books (title, author, picture, quantity, price, inStock, description)
     * Update cart(quantity)
     *
     * OrderItems (id, orderId, book, quantity, price, discount, total, createdBy) create
     * Payment (id, orderId, createdAt, createdBy, deliveryAddress, paymentMethod, deliveryStatus) list
     * DeliveryAddress (createdBy, state, localGov, address, otherInfo, isDeleted) add/delete/list
     */
}
