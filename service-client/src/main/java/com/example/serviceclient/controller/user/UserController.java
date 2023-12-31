package com.example.serviceclient.controller.user;

import com.example.serviceclient.dto.user.request.CreateUserDtoRequest;
import com.example.serviceclient.dto.user.request.LoginRequestDto;
import com.example.serviceclient.dto.user.request.UpdatePasswordDtoRequest;
import com.example.serviceclient.dto.user.request.UpdateUserDtoRequest;
import com.example.serviceclient.dto.user.response.CreateUserDtoResponse;
import com.example.serviceclient.dto.FileResponse;
import com.example.serviceclient.dto.user.response.LoginResponseDto;
import com.example.serviceclient.dto.user.response.UserDtoResponse;
import com.example.serviceclient.security.JwtUtils;
import com.example.serviceclient.security.UserInfoDetails;
import com.example.serviceclient.service.FileService;
import com.example.serviceclient.service.user.UserService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


import java.io.IOException;
import java.net.URI;
import java.security.Principal;
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
    FileService fileService;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    JwtUtils jwtUtils;

    @GetMapping()
    public ResponseEntity<Object> getUser(Principal principal){
        log.info("Received request for user with user: {}", principal.getName());
        UserDtoResponse userDtoResponse = userClientService.getUserByUserName("principal.getName()");
        userDtoResponse.setPassword(null);
        return ResponseEntity.ok(userDtoResponse);
    }

    @PostMapping(value = "/signup", consumes = {MediaType.APPLICATION_JSON_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<Object> createUser( @RequestBody @Valid CreateUserDtoRequest createUserDtoRequest){
        log.info("Received PUT request for user with id: {}", createUserDtoRequest.getFirstName());
        CreateUserDtoResponse response = userClientService.createUser(createUserDtoRequest);
        return ResponseEntity.created(URI.create(String.format("/grpc/users/%s", response.getId()))).body(response);
    }

    @PostMapping(value = "/signin", consumes = {MediaType.APPLICATION_JSON_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<LoginResponseDto> userLogin(@RequestBody @Valid LoginRequestDto loginRequestDto) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequestDto.getEmailAddress(), loginRequestDto.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        UserInfoDetails userDetails = (UserInfoDetails) authentication.getPrincipal();
//        List<String> roles = userDetails.getAuthorities().stream()
//                .map(item -> item.getAuthority())
//                .collect(Collectors.toList());
        UserDtoResponse userDtoResponse =  userClientService.getUserByUserName(userDetails.getUsername());
        LoginResponseDto loginResponseDto = LoginResponseDto.build(userDtoResponse, jwt);
        loginResponseDto.setToken(jwt);
        return ResponseEntity.ok(loginResponseDto);
    }

    @PostMapping("/upload/image")
    public ResponseEntity<FileResponse> fileUpload(@RequestParam("file") MultipartFile file,
                                                   Authentication authentication) throws IOException {
        FileResponse fileResponse = fileService.uploadFile(file);
        return new ResponseEntity<>(fileResponse, HttpStatus.CREATED);
    }

    @PatchMapping(consumes = {MediaType.APPLICATION_JSON_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<LoginResponseDto> updateUser(  @RequestBody @Valid UpdateUserDtoRequest updateUserDtoRequest,
                                                        Principal principal){
        UserDtoResponse response = userClientService.updateUser(updateUserDtoRequest, principal.getName());
        LoginResponseDto loginResponseDto = LoginResponseDto.build(response, null);
        return ResponseEntity.ok(loginResponseDto);
    }

    @PatchMapping(value="/change/password", consumes = {MediaType.APPLICATION_JSON_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<?> updateUserPassword(  @RequestBody @Valid UpdatePasswordDtoRequest updatePasswordDtoRequest,
                                                         Principal principal){
        UserDtoResponse response = userClientService.updateUserPassword(updatePasswordDtoRequest, principal.getName());
        LoginResponseDto loginResponseDto = LoginResponseDto.build(response, null);
        return ResponseEntity.ok(Map.of("message", "Password Successfully Updated!"));
    }
}
