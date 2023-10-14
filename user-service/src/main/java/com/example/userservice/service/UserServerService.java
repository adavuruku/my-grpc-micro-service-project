package com.example.userservice.service;

import com.example.user_service.UserServiceGrpc;
import lombok.extern.log4j.Log4j2;
import net.devh.boot.grpc.server.service.GrpcService;

@GrpcService
@Log4j2
public class UserServerService extends UserServiceGrpc.UserServiceImplBase {

}
