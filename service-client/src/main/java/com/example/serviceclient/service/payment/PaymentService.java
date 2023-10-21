package com.example.serviceclient.service.payment;

import com.example.payment_service.*;
import com.example.serviceclient.dto.book.response.AddCartDtoResponse;
import com.example.serviceclient.dto.book.CustomPagination;
import com.example.serviceclient.dto.book.response.ListCartDtoResponse;
import com.example.serviceclient.dto.payment.request.DeliveryAddressRequestDto;
import com.example.serviceclient.dto.payment.response.DeliveryAddressResponseDto;
import com.example.serviceclient.dto.payment.response.ListDeliveryAddressDtoResponse;
import com.example.serviceclient.exceptions.ServiceExceptionMapper;
import io.grpc.StatusRuntimeException;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
public class PaymentService {
    private final PaymentServiceGrpc.PaymentServiceBlockingStub synchronousClient;

    public PaymentService(PaymentServiceGrpc.PaymentServiceBlockingStub synchronousClient) {
        this.synchronousClient = synchronousClient;
    }

    public DeliveryAddressResponseDto createDeliveryAddress(DeliveryAddressRequestDto deliveryAddressRequestDto, String userName) {
        try {
            // create user message (protobuf)
            DeliveryAddress deliveryAddress = DeliveryAddress.newBuilder()
                    .setLocationAddress(deliveryAddressRequestDto.getLocationAddress())
                    .setLocalGovernment(deliveryAddressRequestDto.getLocalGovernment())
                    .setState(deliveryAddressRequestDto.getState())
                    .setCreatedBy(userName).build();

            // create the request (protobuf) to pass to server
            DeliveryAddress deliveryAddressProto = synchronousClient.createDeliveryAddress(deliveryAddress);

            DeliveryAddressResponseDto deliveryAddressResponseDto =
                    DeliveryAddressResponseDto.fromProtoToObject(deliveryAddressProto);

            return deliveryAddressResponseDto;
        } catch (StatusRuntimeException error) {
            var status = io.grpc.protobuf.StatusProto.fromThrowable(error);
            log.error("Error reason {}", status.getMessage());
            throw ServiceExceptionMapper.map(error);
        }
    }

    public ListDeliveryAddressDtoResponse getAllDeliveryAddress(int page, int perPage, String userName) {
        try {
            //1. Create request and access server
            ListDeliveryAddressRequest listDeliveryAddressRequest = ListDeliveryAddressRequest.newBuilder()
                    .setPage(page).setPerPage(perPage).setUserName(userName).build();

            ListDeliveryAddressResponse listDeliveryAddressResponse = synchronousClient.listDeliveryAddress(listDeliveryAddressRequest);

            //2. convert protobuf to pojo
            List<DeliveryAddressResponseDto> deliveryAddressResponseDtoList = DeliveryAddressResponseDto
                    .fromListOfDeliveryAddressResponseProtoToPojo(listDeliveryAddressResponse.getDeliveryAddressList());

            Pagination pagination = listDeliveryAddressResponse.getPagination();
            CustomPagination customPagination = CustomPagination.builder()
                    .currentPage(pagination.getCurrentPage())
                    .hasNext(pagination.getHasNext()).hasPrevious(pagination.getHasPrevious())
                    .perPage(pagination.getPerPage()).totalPage(pagination.getTotalPage()).totalItem(pagination.getTotalItem()).build();

            ListDeliveryAddressDtoResponse listDeliveryAddressDtoResponse = ListDeliveryAddressDtoResponse.builder()
                    .deliveryAddressResponseDtoList(deliveryAddressResponseDtoList).pagination(customPagination).build();
            return listDeliveryAddressDtoResponse;
        } catch (StatusRuntimeException error) {
            var status = io.grpc.protobuf.StatusProto.fromThrowable(error);
            log.error("Error reason {}", status.getMessage());
            throw ServiceExceptionMapper.map(error);
        }
    }
}
