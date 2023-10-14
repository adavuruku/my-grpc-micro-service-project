package com.example.serviceclient.security;

import com.example.serviceclient.dto.response.UserDtoResponse;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

public class UserInfoDetails implements UserDetails {

    private static final long serialVersionUID = 1L;

    private String id;

    private String name;
    private String password;
    private List<GrantedAuthority> authorities;

    public UserInfoDetails(UserDtoResponse userDtoResponse) {
        name = userDtoResponse.getEmailAddress();
        password = userDtoResponse.getPassword();
//        authorities = Arrays.stream(userInfo.getRoles().split(","))
//                .map(SimpleGrantedAuthority::new)
//                .collect(Collectors.toList());
    }

    public static UserInfoDetails build(UserDtoResponse userDtoResponse) {
//        List<GrantedAuthority> authorities = user.getRoles().stream()
//                .map(role -> new SimpleGrantedAuthority(role.getName().name()))
//                .collect(Collectors.toList());

        return new UserInfoDetails(userDtoResponse);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }


    public String getId() {
        return id;
    }

    @Override
    public String getUsername() {
        return name;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
