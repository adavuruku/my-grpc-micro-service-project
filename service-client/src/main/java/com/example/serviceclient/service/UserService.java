package com.example.serviceclient.service;

import com.example.serviceclient.dto.request.CreateUserDtoRequest;
import com.example.serviceclient.dto.response.CreateUserDtoResponse;
import com.example.serviceclient.dto.response.FileResponse;
import com.example.serviceclient.dto.response.FileResponseSupper;
import com.example.serviceclient.dto.response.UserDtoResponse;
import com.example.serviceclient.exceptions.ServiceExceptionMapper;
import com.example.user_service.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
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

    private final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

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
                .setProfileImage(convertFileResponseToString(userDtoRequest.getProfileImage()))
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
        return userDtoResponse;
    }

    public UserDtoResponse getUser(String id) {
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
                .profileImage(convertStringToFileResponse(responseUser.getProfileImage()))
                .emailAddress(responseUser.getEmailAddress())
                .id(responseUser.getId()).build();
        return userDtoResponse;
    }

    public UserDtoResponse getUserByUserName(String userName) {
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
                .profileImage(convertStringToFileResponse(responseUser.getProfileImage()))
                .lastName(responseUser.getLastName())
                .password(responseUser.getPassword())
                .emailAddress(responseUser.getEmailAddress())
                .id(responseUser.getId()).build();
        return userDtoResponse;
    }

    public FileResponse convertStringToFileResponse(String profileImage){
        FileResponse profileImageUrlTwo = null;
        try {
            if(profileImage != null){
                JsonNode nodeJson = OBJECT_MAPPER.readValue(profileImage, JsonNode.class);
                FileResponseSupper profileImageUrl = OBJECT_MAPPER.treeToValue(nodeJson, FileResponseSupper.class);
                profileImageUrlTwo = profileImageUrl.getFileResponse();
//                System.out.println(nodeJson);
//                profileImageUrl = FileResponse.fromJsonNode(nodeJson);
            }
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        return profileImageUrlTwo;
    }

    public String convertFileResponseToString(FileResponse profileImage){
        String profileImageString = null;
        try {
            if(profileImage != null){
                profileImageString = OBJECT_MAPPER.writeValueAsString(profileImage);
            }
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        return profileImageString;
    }
}
