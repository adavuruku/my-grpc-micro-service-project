package com.example.bookservice.config;

import com.example.bookservice.repo.BooksRepository;
import com.example.bookservice.service.BookServerService;
import net.devh.boot.grpc.server.service.GrpcService;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {
//   TO use constructor injection DONT ADD @GrpcService to service implementing grpc
//   you can do that here
//     If you choose to field injection you dont need this just add @GrpcService to the
    @GrpcService
    public BookServerService createBookService(BooksRepository booksRepository){
        return new BookServerService(booksRepository);
    }
}
