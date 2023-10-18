package com.example.serviceclient.controller.book;

import com.example.serviceclient.dto.book.request.CreateBookDtoRequest;
import com.example.serviceclient.dto.book.response.CreateBookDtoResponse;
import com.example.serviceclient.dto.user.request.CreateUserDtoRequest;
import com.example.serviceclient.dto.user.response.CreateUserDtoResponse;
import com.example.serviceclient.service.book.BookService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.security.Principal;

@RestController
@RequestMapping("/api/v1/book")
@AllArgsConstructor
@Log4j2
@Validated
public class BookController {

    @Autowired
    BookService bookClientService;

    @PostMapping(value = "/add", consumes = {MediaType.APPLICATION_JSON_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<Object> create(@RequestBody @Valid CreateBookDtoRequest createBookDtoRequest, Principal principal){
        CreateBookDtoResponse response = bookClientService.createBook(createBookDtoRequest, principal.getName());
        return ResponseEntity.ok(response);
    }
}
