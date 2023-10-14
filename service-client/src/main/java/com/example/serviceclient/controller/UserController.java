package com.example.serviceclient.controller;

import com.example.serviceclient.dto.request.CreateUserDtoRequest;
import com.example.serviceclient.dto.response.CreateUserDtoResponse;
import com.example.serviceclient.service.UserService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.URI;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/user")
@AllArgsConstructor
@Log4j2
@Validated
public class UserController {

    @Autowired
    UserService userClientService;

    @GetMapping("/{id}")
    public ResponseEntity<Object> get(@PathVariable("id") @NotNull @NotBlank String id) throws IOException {
        log.info("Received request for user with id: {}", id);
        return ResponseEntity.ok(userClientService.getUser(id));
    }

    @PostMapping(consumes = {MediaType.APPLICATION_JSON_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<Object> create( @Valid @RequestBody() CreateUserDtoRequest createUserDtoRequest) throws IOException {
        log.info("Received PUT request for user with id: {}", createUserDtoRequest.getFirstName());
        log.debug("Json String: {}", createUserDtoRequest.toString());
        CreateUserDtoResponse response = userClientService.createUser(createUserDtoRequest);
        return ResponseEntity.created(URI.create(String.format("/grpc/users/%s", response.getId()))).body(response);
    }
}
