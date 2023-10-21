package com.example.serviceclient.dto.payment.response;

import com.example.payment_service.DeliveryAddress;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
public class DeliveryAddressResponseDto {
    private String id;
    private String state;
    private String localGovernment;
    private String locationAddress;
    private String createdAt;
    private String createdBy;

    public static DeliveryAddressResponseDto fromProtoToObject(DeliveryAddress deliveryAddressProto) {
        return DeliveryAddressResponseDto.builder()
                .id(deliveryAddressProto.getId())
                .state(deliveryAddressProto.getState())
                .createdAt(deliveryAddressProto.getCreatedAt())
                .locationAddress(deliveryAddressProto.getLocationAddress())
                .localGovernment(deliveryAddressProto.getLocalGovernment())
                .createdBy(deliveryAddressProto.getCreatedBy()).build();
    }

    public static List<DeliveryAddressResponseDto> fromListOfDeliveryAddressResponseProtoToPojo(List<DeliveryAddress> deliveryAddressList) {
        List<DeliveryAddressResponseDto> deliveryAddressResponseDtoList = new ArrayList<>();
        for(DeliveryAddress deliveryAddress : deliveryAddressList){
            deliveryAddressResponseDtoList.add(
                    DeliveryAddressResponseDto.builder()
                            .id(deliveryAddress.getId())
                            .state(deliveryAddress.getState())
                            .localGovernment(deliveryAddress.getLocalGovernment())
                            .locationAddress(deliveryAddress.getLocationAddress())
                            .createdAt(deliveryAddress.getCreatedAt())
                            .createdBy(deliveryAddress.getCreatedBy()).build()
            );
        }
        return deliveryAddressResponseDtoList;
    }
}
