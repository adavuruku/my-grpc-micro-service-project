package com.example.userservice.service;

import com.example.user_service.*;
import com.example.userservice.constants.CommonConstants;
import com.example.userservice.exceptions.ErrorCode;
import com.example.userservice.exceptions.ResourceNotFoundException;
import com.example.userservice.repo.UsersRepository;
import com.example.userservice.schema.UserSchema;
import io.grpc.stub.StreamObserver;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.dao.DuplicateKeyException;

import java.util.Map;

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
    public void createUser(CreateUserRequest createOrSaveUserRequest, StreamObserver<CreateUserResponse> responseObserver) {

        try {
            User userRequest = createOrSaveUserRequest.getUser();


            UserSchema userSchema = UserSchema.builder()
                    .emailAddress(userRequest.getEmailAddress())
                    .firstName(userRequest.getFirstName())
                    .lastName(userRequest.getLastName())
                    .phoneNumber(userRequest.getPhoneNumber())
                    .password(userRequest.getPassword())
                    .profileImage(userRequest.getProfileImage())
                    .contactAddress(userRequest.getContactAddress())
                    .build();
            UserSchema userDto = usersRepository.save(userSchema);
            CreateUserResponse createOrSaveUserResponse = CreateUserResponse.newBuilder().setId(userDto.getId()).setStatusMessage(CommonConstants.SUCCESSFUL_PUT_MESSAGE).build();
            responseObserver.onNext(createOrSaveUserResponse);
            responseObserver.onCompleted();
        } catch (DuplicateKeyException e) {
//            e.printStackTrace();
            throw new com.example.userservice.exceptions.DuplicateKeyException(ErrorCode.USER_ALREADY_EXISTS,
                    ErrorCode.USER_ALREADY_EXISTS.getMessage(), Map.of("Email Addrress: ", createOrSaveUserRequest.getUser().getEmailAddress(), "message", "User with Email Address: "+createOrSaveUserRequest.getUser().getEmailAddress()+" already exist"));
        } catch (Exception e) {
//            e.printStackTrace();
            throw e;
        }
    }

    @Override
    public void getUserByUsername(GetUserByUserNameRequest request, StreamObserver<User> responseObserver) {
        try {
            String emailAddress = request.getEmailAddress();
            UserSchema data = usersRepository.findUserByEmailAddress(emailAddress);
            if(data == null ){
                throw new ResourceNotFoundException("Resource not found.",  Map.of("User name", emailAddress, "message", "Resource Not Found"));
            }
            User responseUser = User.newBuilder().setId(data.getId())
                    .setEmailAddress(data.getEmailAddress())
                    .setContactAddress(data.getContactAddress())
                    .setFirstName(data.getFirstName())
                    .setLastName(data.getLastName())
                    .setPassword(data.getPassword())
                    .setProfileImage(data.getProfileImage())
                    .setPhoneNumber(data.getPhoneNumber()).build();
            responseObserver.onNext(responseUser);
            responseObserver.onCompleted();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }

    }

    @Override
    public void updateUser(UpdateUserRequest updateUserRequest, StreamObserver<User> updateUserResponse) {
        try {
            UserSchema data = usersRepository.findUserByEmailAddress(updateUserRequest.getEmailAddress());
            if(data == null ){
                throw new ResourceNotFoundException("Resource not found.",  Map.of("User name", updateUserRequest.getEmailAddress(), "message", "Resource Not Found"));
            }
            data.setContactAddress(updateUserRequest.getContactAddress());
            data.setFirstName(updateUserRequest.getFirstName());
            data.setPhoneNumber(updateUserRequest.getPhoneNumber());
            data.setLastName(updateUserRequest.getLastName());
            data.setProfileImage(updateUserRequest.getProfileImage());
            UserSchema userDto = usersRepository.save(data);
            User updateAUserResponse = User.newBuilder().setId(userDto.getId())
                    .setEmailAddress(userDto.getEmailAddress())
                    .setContactAddress(userDto.getContactAddress())
                    .setFirstName(userDto.getFirstName())
                    .setLastName(userDto.getLastName())
                    .setPassword(userDto.getPassword())
                    .setProfileImage(userDto.getProfileImage())
                    .setPhoneNumber(userDto.getPhoneNumber()).build();
            updateUserResponse.onNext(updateAUserResponse);
            updateUserResponse.onCompleted();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }

    }

    @Override
    public void updateUserPassword(ChangePasswordRequest changePasswordRequest, StreamObserver<User> updateUserResponse) {
        try {
            UserSchema data = usersRepository.findUserByEmailAddress(changePasswordRequest.getEmailAddress());
            if(data == null ){
                throw new ResourceNotFoundException("Resource not found.",  Map.of("User name", changePasswordRequest.getEmailAddress(), "message", "Resource Not Found"));
            }
            data.setPassword(changePasswordRequest.getPassword());

            UserSchema userDto = usersRepository.save(data);
            User updateAUserResponse = User.newBuilder().setId(userDto.getId())
                    .setEmailAddress(userDto.getEmailAddress())
                    .setContactAddress(userDto.getContactAddress())
                    .setFirstName(userDto.getFirstName())
                    .setLastName(userDto.getLastName())
                    .setPassword(userDto.getPassword())
                    .setProfileImage(userDto.getProfileImage())
                    .setPhoneNumber(userDto.getPhoneNumber()).build();
            updateUserResponse.onNext(updateAUserResponse);
            updateUserResponse.onCompleted();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
}
