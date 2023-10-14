package com.example.userservice.config;

import com.example.userservice.repo.UsersRepository;
import com.example.userservice.service.UserServerService;
import net.devh.boot.grpc.server.service.GrpcService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {
//   TO use constructor injection DONT ADD @GrpcService to service implementing grpc
//   you can do that here
//     If you choose to field injection you dont need this just add @GrpcService to the
    @GrpcService
    public UserServerService createUserService(UsersRepository usersRepository){
        return new UserServerService(usersRepository);
    }
}
