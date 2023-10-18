package com.example.serviceclient.security;

import com.example.serviceclient.dto.user.response.UserDtoResponse;
import com.example.serviceclient.service.user.UserService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

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
