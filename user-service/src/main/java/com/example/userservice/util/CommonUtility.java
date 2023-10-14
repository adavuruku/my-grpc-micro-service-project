package com.example.userservice.util;

import com.example.user_service.User;
import com.example.userservice.dto.UserDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CommonUtility {

//    @Autowired
//    public Gson customGsonBuilder;
//    public User convertToProtoBuf(UserPojo userPojo) throws JsonProcessingException {
//        ObjectMapper mapper = new ObjectMapper();
//        String jsonString = mapper.writeValueAsString(userPojo);
//        return customGsonBuilder.fromJson(jsonString, User.class);
//    }

//    public UserDto convertToPojo(User user) throws IOException {
//        ObjectMapper mapper = new ObjectMapper();
//        String jsonString = customGsonBuilder.toJson(user);
//        return mapper.readValue(jsonString, UserPojo.class);
//    }


}
