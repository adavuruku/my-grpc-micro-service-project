package com.example.serviceclient.config;

import com.example.serviceclient.security.UserInfoSecurityService;
import com.example.serviceclient.service.UserService;
import com.example.user_service.UserServiceGrpc;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class AppConfig {
    @Bean
    public UserService createUserService(@GrpcClient("user-service") UserServiceGrpc.UserServiceBlockingStub synchronousClient,
    PasswordEncoder encoder){
        return new UserService(synchronousClient, encoder);
    }

    @Bean
    public UserInfoSecurityService createService(UserService userClientService){
        return new UserInfoSecurityService(userClientService);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
