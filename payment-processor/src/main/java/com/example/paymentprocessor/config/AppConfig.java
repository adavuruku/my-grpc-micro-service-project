package com.example.paymentprocessor.config;

import com.example.paymentprocessor.repo.DeliveryAddressRepo;
import com.example.paymentprocessor.repo.TransactionRepo;
import com.example.paymentprocessor.service.PaymentService;
import net.devh.boot.grpc.server.service.GrpcService;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {
//   TO use constructor injection DONT ADD @GrpcService to service implementing grpc
//   you can do that here
//     If you choose to field injection you dont need this just add @GrpcService to the
    @GrpcService
    public PaymentService createPaymentService(DeliveryAddressRepo deliveryAddressRepo, TransactionRepo transactionRepo){
        return new PaymentService(deliveryAddressRepo, transactionRepo);
    }
}
