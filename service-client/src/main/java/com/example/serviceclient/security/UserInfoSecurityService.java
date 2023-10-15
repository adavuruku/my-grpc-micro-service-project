package com.example.serviceclient.security;

import com.example.serviceclient.dto.response.UserDtoResponse;
import com.example.serviceclient.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

public class UserInfoSecurityService implements UserDetailsService {

    private final UserService userClientService;
    public UserInfoSecurityService(UserService userClientService){
        this.userClientService = userClientService;
    }
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserDtoResponse userDtoResponse = userClientService.getUserByUserName(username);
        if(userDtoResponse == null){
            throw new UsernameNotFoundException("User not found " + username);
        }
        return UserInfoDetails.build(userDtoResponse);
    }
}
