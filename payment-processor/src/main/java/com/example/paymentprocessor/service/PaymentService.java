package com.example.paymentprocessor.service;

import com.example.payment_service.*;
import com.example.paymentprocessor.constants.GenerateTransactionReference;
import com.example.paymentprocessor.exceptions.ResourceNotFoundException;
import com.example.paymentprocessor.repo.DeliveryAddressRepo;
import com.example.paymentprocessor.repo.TransactionRepo;
import com.example.paymentprocessor.schema.DeliveryAddressSchema;
import com.example.paymentprocessor.schema.TransactionSchema;
import io.grpc.stub.StreamObserver;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@AllArgsConstructor
@Log4j2
public class PaymentService extends PaymentServiceGrpc.PaymentServiceImplBase {
    private final DeliveryAddressRepo deliveryAddressRepo;
    private final TransactionRepo transactionRepo;

    /**
     * Transaction Methods begins
     * **/
    public void createTransaction(Transaction transaction, StreamObserver<TransactionResponse> transactionResponseStreamObserver){
        try{
            Optional<DeliveryAddressSchema> deliveryAddressSchema = deliveryAddressRepo.findByIdAndCreatedBy(transaction.getDeliveryAddress(), transaction.getCreatedBy());
            if(deliveryAddressSchema.isEmpty()){
                throw new ResourceNotFoundException("Resource not found.",  Map.of("Delivery Address with Id: ", transaction.getDeliveryAddress(), "message", "Resource Not Found"));
            }
            TransactionSchema transactionSchema = TransactionSchema.convertTransactionProtoToTransactionSchema(
                    transaction
            );
            transactionSchema.setDeliveryAddressSchema(deliveryAddressSchema.get());
            transactionSchema.setTransactionRef(GenerateTransactionReference.next());
            TransactionSchema transactionSchemaData = transactionRepo.save(transactionSchema);

            TransactionResponse transactionResponse = TransactionSchema.convertTransactionSchemaToTransactionProto(
                    transactionSchemaData
            );
            transactionResponseStreamObserver.onNext(transactionResponse);
            transactionResponseStreamObserver.onCompleted();
        }catch (Exception e) {
            throw e;
        }
    }

    public void updateTransactionDeliveryStatus(UpdateDeliveryStatusRequest updateDeliveryStatusRequest,
                                                StreamObserver<TransactionResponse> transactionResponseStreamObserver){
        Optional<TransactionSchema> transactionSchema = transactionRepo.findByIdAndTransactionRef(updateDeliveryStatusRequest.getId(),updateDeliveryStatusRequest.getTransactionRef());
        if(transactionSchema.isEmpty()){
            throw new ResourceNotFoundException("Resource not found.",  Map.of("Transaction request details : ", "ID: " + updateDeliveryStatusRequest.getId()+" Transaction Ref: "+updateDeliveryStatusRequest.getTransactionRef(), "message", "Resource Not Found"));
        }

        TransactionSchema transactionSchemaData = transactionSchema.get();
        transactionSchemaData.setDeliveryStatus(updateDeliveryStatusRequest.getDeliveryStatus());
        transactionSchemaData.setReceiptPaid(false);
        if(updateDeliveryStatusRequest.getDeliveryStatus()){
            transactionSchemaData.setReceiptPaid(true);
            transactionSchemaData.setPaymentMethod(updateDeliveryStatusRequest.getPaymentMethod());
        }
        transactionRepo.save(transactionSchemaData);

        TransactionResponse transactionResponse = TransactionSchema.convertTransactionSchemaToTransactionProto(transactionSchemaData);
        transactionResponseStreamObserver.onNext(transactionResponse);
        transactionResponseStreamObserver.onCompleted();
    }

    public void listTransaction(ListTransactionRequest listTransactionRequest,
                                StreamObserver<ListTransactionResponse> listTransactionResponseStreamObserver){
        Sort sort = Sort.by(Sort.Direction.DESC, "createdAt");
        Pageable paging = PageRequest.of(listTransactionRequest.getPage(), listTransactionRequest.getPerPage(), sort);
        Page<TransactionSchema> pageTransactionSchema = transactionRepo.findByCreatedBy(listTransactionRequest.getUserName(), paging);

        List<TransactionSchema> transactionSchemaList = pageTransactionSchema.getContent();

        List<TransactionResponse> transactionList = TransactionSchema.convertListOfTransactionSchemaToListOfTransactionResponseProto(
                transactionSchemaList
        );
        Pagination pagination = Pagination.newBuilder().setPerPage(pageTransactionSchema.getSize())
                .setCurrentPage(pageTransactionSchema.getNumber()).setTotalPage(pageTransactionSchema.getTotalPages())
                .setHasNext(pageTransactionSchema.hasNext()).setHasPrevious(pageTransactionSchema.hasPrevious())
                .setTotalItem(pageTransactionSchema.getTotalElements()).build();

        ListTransactionResponse listTransactionResponse = ListTransactionResponse.newBuilder()
                .addAllTransactionsResponse(transactionList).setPagination(pagination).build();

        listTransactionResponseStreamObserver.onNext(listTransactionResponse);
        listTransactionResponseStreamObserver.onCompleted();

    }

    public void searchTransactionReference(SearchTransactionRequest searchTransactionRequest,
                                           StreamObserver<TransactionResponse> transactionResponseStreamObserver){
        Optional<TransactionSchema> transactionSchema = transactionRepo.findByCreatedByAndTransactionRef(searchTransactionRequest.getUserName(), searchTransactionRequest.getTransactionReference());

        if(transactionSchema.isEmpty()){
            throw new ResourceNotFoundException("Resource not found.",  Map.of("Transaction request details : ", " Transaction Ref: "+searchTransactionRequest.getTransactionReference(), "message", "Resource Not Found"));
        }

        TransactionResponse transactionResponse = TransactionSchema.convertTransactionSchemaToTransactionProto(transactionSchema.get());
        transactionResponseStreamObserver.onNext(transactionResponse);
        transactionResponseStreamObserver.onCompleted();

    }
    /**
     * Delivery Address Methods begins
     * **/
    public void createDeliveryAddress(DeliveryAddress deliveryAddress, StreamObserver<DeliveryAddress> deliveryAddressStreamObserver) {
        try{
            DeliveryAddressSchema deliveryAddressSchema = DeliveryAddressSchema.builder()
                    .state(deliveryAddress.getState())
                    .locationAddress(deliveryAddress.getLocationAddress())
                    .localGovernment(deliveryAddress.getLocalGovernment())
                    .createdAt(new Date().toString())
                    .createdBy(deliveryAddress.getCreatedBy()).build();

            DeliveryAddressSchema deliveryAddressSchemaData = deliveryAddressRepo.save(deliveryAddressSchema);

            //2. Convert schemas to proto for data transport
            DeliveryAddress deliveryAddressProto = DeliveryAddressSchema.convertDeliveryAddressSchemaToDeliveryAddressProto(
                    deliveryAddressSchemaData
            );
            deliveryAddressStreamObserver.onNext(deliveryAddressProto);
            deliveryAddressStreamObserver.onCompleted();
        }catch (Exception e) {
            throw e;
        }
    }

    public void listDeliveryAddress(ListDeliveryAddressRequest listDeliveryAddressRequest,
                                    StreamObserver<ListDeliveryAddressResponse> listDeliveryAddressResponseStreamObserver) {
        try{
            //1. Paginate and get records from DB
            Sort sort = Sort.by(Sort.Direction.DESC, "createdAt");
            Pageable paging = PageRequest.of(listDeliveryAddressRequest.getPage(), listDeliveryAddressRequest.getPerPage(), sort);
            Page<DeliveryAddressSchema> pageDeliveryAddressSchema = deliveryAddressRepo.findByCreatedBy(listDeliveryAddressRequest.getUserName(), paging);

            List<DeliveryAddressSchema> deliveryAddressSchemaList = pageDeliveryAddressSchema.getContent();
            Pagination pagination = Pagination.newBuilder().setPerPage(pageDeliveryAddressSchema.getSize())
                    .setCurrentPage(pageDeliveryAddressSchema.getNumber()).setTotalPage(pageDeliveryAddressSchema.getTotalPages())
                    .setHasNext(pageDeliveryAddressSchema.hasNext()).setHasPrevious(pageDeliveryAddressSchema.hasPrevious())
                    .setTotalItem(pageDeliveryAddressSchema.getTotalElements()).build();

            //2. Convert schemas to proto for data transport
            List<DeliveryAddress> deliveryAddressList = DeliveryAddressSchema.convertListOfDeliveryAddressSchemaToListOfDeliveryAddressProto(
                    deliveryAddressSchemaList
            );

            ListDeliveryAddressResponse listDeliveryAddressResponse = ListDeliveryAddressResponse.newBuilder()
                    .addAllDeliveryAddress(deliveryAddressList)
                    .setPagination(pagination).build();

            listDeliveryAddressResponseStreamObserver.onNext(listDeliveryAddressResponse);
            listDeliveryAddressResponseStreamObserver.onCompleted();

        }catch (Exception e) {
            throw e;
        }
    }


}
