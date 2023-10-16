package com.example.userservice.service;

import com.example.user_service.*;
import com.example.userservice.constants.CommonConstants;
import com.example.userservice.dto.UserDto;
import com.example.userservice.exceptions.ResourceNotFoundException;
import com.example.userservice.repo.UserRepositoryCollections;
import com.example.userservice.repo.UsersRepository;
import com.example.userservice.schema.UserSchema;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.grpc.stub.StreamObserver;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import net.devh.boot.grpc.server.service.GrpcService;

import java.io.IOException;
import java.util.Map;
import java.util.Optional;

//@GrpcService
@AllArgsConstructor
@Log4j2
public class UserServerService extends UserServiceGrpc.UserServiceImplBase {
    private final UsersRepository usersRepository;
    private final UserRepositoryCollections userRepositoryCollections;

//    public UserServerService(UsersRepository usersRepository){
//        super();
//        this.usersRepository = usersRepository;
//    }
    @Override
    public void createUser(CreateUserRequest createOrSaveUserRequest, StreamObserver<CreateUserResponse> responseObserver) {
        User userRequest = createOrSaveUserRequest.getUser();
        UserSchema userSchema;
        CreateUserResponse createOrSaveUserResponse = null;

        try {
            userSchema = UserSchema.builder()
                    .emailAddress(userRequest.getEmailAddress())
                    .firstName(userRequest.getFirstName())
                    .lastName(userRequest.getLastName())
                    .phoneNumber(userRequest.getPhoneNumber())
                    .password(userRequest.getPassword())
                    .profileImage(userRequest.getProfileImage())
                    .contactAddress(userRequest.getContactAddress())
                    .build();
            UserSchema userDto = usersRepository.save(userSchema);
            createOrSaveUserResponse = CreateUserResponse.newBuilder().setId(userDto.getId()).setStatusMessage(CommonConstants.SUCCESSFUL_PUT_MESSAGE).build();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        responseObserver.onNext(createOrSaveUserResponse);
        responseObserver.onCompleted();
    }

    @Override
    public void getUserByUsername(GetUserByUserNameRequest request, StreamObserver<User> responseObserver) {
        String emailAddress = request.getEmailAddress();
        UserSchema data = usersRepository.findUserByEmailAddress(emailAddress);
        if(data == null ){
            throw new ResourceNotFoundException("Resource not found.",  Map.of("User name", emailAddress, "message", "Resource Not Found"));
        }
        User responseUser = null;
        try {
            responseUser = User.newBuilder().setId(data.getId())
                    .setEmailAddress(data.getEmailAddress())
                    .setContactAddress(data.getContactAddress())
                    .setFirstName(data.getFirstName())
                    .setLastName(data.getLastName())
                    .setPassword(data.getPassword())
                    .setProfileImage(data.getProfileImage())
                    .setPhoneNumber(data.getPhoneNumber()).build();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        responseObserver.onNext(responseUser);
        responseObserver.onCompleted();
    }

    @Override
    public void updateUser(UpdateUserRequest updateUserRequest, StreamObserver<User> updateUserResponse) {
        User updateAUserResponse = null;
        try {
            UserSchema data = usersRepository.findUserByEmailAddress(updateUserRequest.getEmailAddress());
            if(data == null ){
                throw new ResourceNotFoundException("Resource not found.",  Map.of("User name", updateUserRequest.getEmailAddress(), "message", "Resource Not Found"));
            }
            if(updateUserRequest.getContactAddress() != null){
                data.setContactAddress(updateUserRequest.getContactAddress());
            }
            if(updateUserRequest.getFirstName() != null){
                data.setFirstName(updateUserRequest.getFirstName());
            }
            if(updateUserRequest.getLastName() != null){
                data.setLastName(updateUserRequest.getLastName());
            }
            if(updateUserRequest.getPhoneNumber() != null){
                data.setPhoneNumber(updateUserRequest.getPhoneNumber());
            }
            if(updateUserRequest.getProfileImage() != null){
                data.setProfileImage(updateUserRequest.getProfileImage());
            }
            UserSchema userDto = usersRepository.save(data);
            updateAUserResponse = User.newBuilder().setId(userDto.getId())
                    .setEmailAddress(userDto.getEmailAddress())
                    .setContactAddress(userDto.getContactAddress())
                    .setFirstName(userDto.getFirstName())
                    .setLastName(userDto.getLastName())
                    .setPassword(userDto.getPassword())
                    .setProfileImage(userDto.getProfileImage())
                    .setPhoneNumber(userDto.getPhoneNumber()).build();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        updateUserResponse.onNext(updateAUserResponse);
        updateUserResponse.onCompleted();
    }
}
