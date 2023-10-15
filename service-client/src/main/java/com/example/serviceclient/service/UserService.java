package com.example.serviceclient.service;

import com.example.serviceclient.dto.request.CreateUserDtoRequest;
import com.example.serviceclient.dto.response.CreateUserDtoResponse;
import com.example.serviceclient.dto.response.UserDtoResponse;
import com.example.serviceclient.exceptions.ServiceExceptionMapper;
import com.example.user_service.*;
import io.grpc.StatusRuntimeException;
import lombok.extern.log4j.Log4j2;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;


@Log4j2
public class UserService {
    private final UserServiceGrpc.UserServiceBlockingStub synchronousClient;
    private final PasswordEncoder encoder;

    public UserService(UserServiceGrpc.UserServiceBlockingStub synchronousClient, PasswordEncoder encoder){
        this.synchronousClient = synchronousClient;
        this.encoder = encoder;
    }

    public CreateUserDtoResponse createUser(CreateUserDtoRequest userDtoRequest) {
        // create user message (protobuf)
        User user = User.newBuilder()
                .setPhoneNumber(userDtoRequest.getPhoneNumber())
                .setLastName(userDtoRequest.getLastName())
                .setFirstName(userDtoRequest.getFirstName())
                .setContactAddress(userDtoRequest.getContactAddress())
                .setEmailAddress(userDtoRequest.getEmailAddress())
                .setPassword(encoder.encode(userDtoRequest.getPassword()))
                .build();
        // create the request (protobuf) to pass to server
        CreateOrSaveUserRequest createOrSaveUserRequest = CreateOrSaveUserRequest.newBuilder().setUser(user).build();
        CreateOrSaveUserResponse createOrSaveUserResponse ;
        try {
            createOrSaveUserResponse = synchronousClient.createUser(createOrSaveUserRequest);
        } catch (StatusRuntimeException error) {
            log.error("Error while creating user, reason {} ", error.getMessage());
            throw ServiceExceptionMapper.map(error);
        }

        CreateUserDtoResponse userDtoResponse = CreateUserDtoResponse.builder()
                .id(createOrSaveUserResponse.getId()).statusMessage("Successfully Created").build();
        log.info("Successfully created new user. id: {}", createOrSaveUserResponse.getId());
        return userDtoResponse;
    }

    public UserDtoResponse getUser(String id) {
        log.info("Processing GET request for user id: {}", id);
        GetUserRequest getUserRequest = GetUserRequest.newBuilder().setId(id).build();
        User responseUser;
        try {
            responseUser = synchronousClient.getUser(getUserRequest);
        } catch (StatusRuntimeException error) {
            log.error("Error while getting user details, reason {} ", error.getMessage());
            throw ServiceExceptionMapper.map(error);
        }
        UserDtoResponse userDtoResponse = UserDtoResponse.builder()
                .contactAddress(responseUser.getContactAddress())
                .phoneNumber(responseUser.getPhoneNumber())
                .firstName(responseUser.getFirstName())
                .lastName(responseUser.getLastName())
                .emailAddress(responseUser.getEmailAddress())
                .id(responseUser.getId()).build();
        log.debug("Got response: {}", responseUser.toString());
        return userDtoResponse;
    }

    public UserDtoResponse getUserByUserName(String userName) {
        log.info("Auth is here Processing GET request for user id: {}", userName);
        GetUserByUserNameRequest getUserByUserNameRequest = GetUserByUserNameRequest.newBuilder().setEmailAddress(userName).build();
        User responseUser;
        try {
            responseUser = synchronousClient.getUserByUsername(getUserByUserNameRequest);
        } catch (StatusRuntimeException error) {
            log.error("Error while getting user details, reason {} ", error.getMessage());
            throw ServiceExceptionMapper.map(error);
        }
        UserDtoResponse userDtoResponse = UserDtoResponse.builder()
                .contactAddress(responseUser.getContactAddress())
                .phoneNumber(responseUser.getPhoneNumber())
                .firstName(responseUser.getFirstName())
                .lastName(responseUser.getLastName())
                .password(responseUser.getPassword())
                .emailAddress(responseUser.getEmailAddress())
                .id(responseUser.getId()).build();
        log.debug("Got response: {}", responseUser.toString());
        return userDtoResponse;
    }
}
