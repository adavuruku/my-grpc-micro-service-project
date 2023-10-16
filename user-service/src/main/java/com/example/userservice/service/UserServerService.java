package com.example.userservice.service;

import com.example.user_service.*;
import com.example.userservice.constants.CommonConstants;
import com.example.userservice.dto.UserDto;
import com.example.userservice.exceptions.ResourceNotFoundException;
import com.example.userservice.repo.UsersRepository;
import com.example.userservice.schema.UserSchema;
import com.fasterxml.jackson.core.JsonProcessingException;
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

//    public UserServerService(UsersRepository usersRepository){
//        super();
//        this.usersRepository = usersRepository;
//    }
    @Override
    public void createUser(CreateOrSaveUserRequest createOrSaveUserRequest, StreamObserver<CreateOrSaveUserResponse> responseObserver) {
        User userRequest = createOrSaveUserRequest.getUser();
        UserSchema userSchema;
        CreateOrSaveUserResponse createOrSaveUserResponse = null;

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
            createOrSaveUserResponse = CreateOrSaveUserResponse.newBuilder().setId(userDto.getId()).setStatusMessage(CommonConstants.SUCCESSFUL_PUT_MESSAGE).build();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        responseObserver.onNext(createOrSaveUserResponse);
        responseObserver.onCompleted();
    }

    @Override
    public void getUser(GetUserRequest request, StreamObserver<User> responseObserver) {
        String id = request.getId();
        Optional<UserSchema> userPojo = usersRepository.findById(id);
        if(!userPojo.isPresent()){
            throw new ResourceNotFoundException("Resource not found.",  Map.of("id", id, "message", "Resource Not Found"));
        }
        User responseUser = null;
        try {
            UserSchema data = userPojo.get();
            responseUser = User.newBuilder().setId(data.getId())
                    .setEmailAddress(data.getEmailAddress())
                    .setContactAddress(data.getContactAddress())
                    .setFirstName(data.getFirstName())
                    .setLastName(data.getLastName())
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

}
