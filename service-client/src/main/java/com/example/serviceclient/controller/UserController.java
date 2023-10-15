package com.example.serviceclient.controller;

import com.example.serviceclient.dto.request.CreateUserDtoRequest;
import com.example.serviceclient.dto.request.LoginRequestDto;
import com.example.serviceclient.dto.response.CreateUserDtoResponse;
import com.example.serviceclient.dto.response.LoginResponseDto;
import com.example.serviceclient.dto.response.UserDtoResponse;
import com.example.serviceclient.security.JwtUtils;
import com.example.serviceclient.security.UserInfoDetails;
import com.example.serviceclient.service.UserService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.URI;
import java.security.Principal;
import java.text.ParseException;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/user")
@AllArgsConstructor
@Log4j2
@Validated
public class UserController {

    @Autowired
    UserService userClientService;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    JwtUtils jwtUtils;

    @GetMapping()
    public ResponseEntity<Object> getUser(Principal principal){
        log.info("Received request for user with id: {}", principal.getName());
        UserDtoResponse userDtoResponse = userClientService.getUserByUserName(principal.getName());
        userDtoResponse.setPassword(null);
        return ResponseEntity.ok(userDtoResponse);
    }

    @PostMapping(value = "/signup", consumes = {MediaType.APPLICATION_JSON_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<Object> create( @Valid @RequestBody() CreateUserDtoRequest createUserDtoRequest){
        log.info("Received PUT request for user with id: {}", createUserDtoRequest.getFirstName());
        CreateUserDtoResponse response = userClientService.createUser(createUserDtoRequest);
        return ResponseEntity.created(URI.create(String.format("/grpc/users/%s", response.getId()))).body(response);
    }

    @PostMapping(value = "/signin", consumes = {MediaType.APPLICATION_JSON_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<?> userLogin(@Valid @RequestBody LoginRequestDto loginRequestDto) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequestDto.getEmailAddress(), loginRequestDto.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        UserInfoDetails userDetails = (UserInfoDetails) authentication.getPrincipal();
//        List<String> roles = userDetails.getAuthorities().stream()
//                .map(item -> item.getAuthority())
//                .collect(Collectors.toList());
        UserDtoResponse userDtoResponse =  userClientService.getUserByUserName(userDetails.getUsername());
        LoginResponseDto loginResponseDto = LoginResponseDto.builder()
                .token(jwt).firstName(userDtoResponse.getFirstName()).lastName(userDtoResponse.getLastName())
                .contactAddress(userDtoResponse.getContactAddress())
                .emailAddress(userDtoResponse.getEmailAddress()).phoneNumber(userDtoResponse.getPhoneNumber())
                .id(userDtoResponse.getId()).build();
        return ResponseEntity.ok(loginResponseDto);
    }
}
