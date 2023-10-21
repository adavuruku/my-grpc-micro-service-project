package com.example.serviceclient.dto.payment.response;

import com.example.serviceclient.dto.book.CustomPagination;
import com.example.serviceclient.dto.book.response.AddCartDtoResponse;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class ListDeliveryAddressDtoResponse {
    private List<DeliveryAddressResponseDto> deliveryAddressResponseDtoList;
    private CustomPagination pagination;
}
