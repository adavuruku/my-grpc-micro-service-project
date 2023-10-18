package com.example.serviceclient.dto.user.response;

import com.example.serviceclient.dto.FileResponse;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LoginResponseDto {
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String id;
    private FileResponse profileImage;
    private String contactAddress;
    private String emailAddress;
    private String token;
    private String type = "Bearer";

    public static LoginResponseDto build(UserDtoResponse userDtoResponse, String token){

        return LoginResponseDto.builder()
                .firstName(userDtoResponse.getFirstName())
                .lastName(userDtoResponse.getLastName())
                .phoneNumber(userDtoResponse.getPhoneNumber())
                .id(userDtoResponse.getId())
                .profileImage(userDtoResponse.getProfileImage())
                .contactAddress(userDtoResponse.getContactAddress())
                .emailAddress(userDtoResponse.getEmailAddress())
                .token(token)
                .type("Bearer")
                .build();
    }
}
