package com.example.userservice.schema;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.HashMap;
import java.util.Map;

@Data
@Document("users")
@Builder
public class UserSchema {
    @Id
    private String id;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String password;
    private String profileImage;
    @Indexed(unique = true)
    private String emailAddress;
    private String contactAddress;
}
