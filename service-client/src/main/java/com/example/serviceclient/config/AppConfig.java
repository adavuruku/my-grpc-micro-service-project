package com.example.serviceclient.config;

import com.cloudinary.Cloudinary;
import com.example.book_service.BookServiceGrpc;
import com.example.payment_service.PaymentServiceGrpc;
import com.example.serviceclient.security.UserInfoSecurityService;
import com.example.serviceclient.service.FileService;
import com.example.serviceclient.service.book.BookService;
import com.example.serviceclient.service.payment.PaymentService;
import com.example.serviceclient.service.user.UserService;
import com.example.user_service.UserServiceGrpc;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class AppConfig {
    @Bean
    public UserService createUserService(@GrpcClient("user-service") UserServiceGrpc.UserServiceBlockingStub synchronousClient,
    PasswordEncoder encoder){
        return new UserService(synchronousClient, encoder);
    }

    @Bean
    public BookService createBookService(@GrpcClient("book-service") BookServiceGrpc.BookServiceBlockingStub synchronousClient){
        return new BookService(synchronousClient);
    }

    @Bean
    public PaymentService createPaymentService(@GrpcClient("payment-service") PaymentServiceGrpc.PaymentServiceBlockingStub synchronousClient){
        return new PaymentService(synchronousClient);
    }
    @Bean
    public UserInfoSecurityService createService(UserService userClientService){
        return new UserInfoSecurityService(userClientService);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    @Bean
    public Cloudinary cloudinaryConfig(@Value("${file-server.cloudinary.cloud-name}") String cloudName,
                                       @Value("${file-server.cloudinary.api-key}") String apiKey,
                                       @Value("${file-server.cloudinary.api-secret}") String apiSecret) {
        Cloudinary cloudinary = null;
        Map config = new HashMap();
        config.put("cloud_name", cloudName);
        config.put("api_key", apiKey);
        config.put("api_secret", apiSecret);
        config.put("secure", true);
        cloudinary = new Cloudinary(config);
        return cloudinary;
    }

    @Bean
    public FileService createFileService(Cloudinary cloudinaryConfig){
        return new FileService(cloudinaryConfig);
    }
}
