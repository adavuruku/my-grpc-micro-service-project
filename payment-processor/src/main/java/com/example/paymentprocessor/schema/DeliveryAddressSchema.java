package com.example.paymentprocessor.schema;

import com.example.book_service.Book;
import com.example.book_service.BookImage;
import com.example.payment_service.DeliveryAddress;
import lombok.Builder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Data
@Document("deliver_address")
@Builder
//@NoArgsConstructor
public class DeliveryAddressSchema {
    @Id
    private String id;
    private String state;
    private String localGovernment;
    private String createdAt;
    private String createdBy;
    private String locationAddress;
//    public static DeliveryAddressSchema convertBookProtoToBookSchema(Book bookRequest, List<BookImageSchema> bookImageSchemaList){
////        log.info("{}", bookRequest);
//        return DeliveryAddressSchema.builder()
//                .title(bookRequest.getTitle())
//                .description(bookRequest.getDescription())
//                .authors(bookRequest.getAuthorsList())
//                .quantity(bookRequest.getQuantity())
//                .discount(bookRequest.getDiscount())
//                .price(bookRequest.getPrice())
//                .inStock(bookRequest.getInStock())
//                .bookSlug(bookRequest.getBookSlug())
//                .isbn(bookRequest.getIsbn())
//                .bookImages(bookImageSchemaList)
//                .createdAt(bookRequest.getCreatedAt())
//                .createdBy(bookRequest.getCreatedBy())
//                .isDeleted(false)
//                .build();
//    }
//
    public static DeliveryAddress convertDeliveryAddressSchemaToDeliveryAddressProto(DeliveryAddressSchema deliveryAddressSchema){
        return DeliveryAddress.newBuilder()
                .setId(deliveryAddressSchema.getId())
                .setCreatedAt(deliveryAddressSchema.getCreatedAt())
                .setCreatedBy(deliveryAddressSchema.getCreatedBy())
                .setState(deliveryAddressSchema.getState())
                .setLocationAddress(deliveryAddressSchema.getLocationAddress())
                .setLocalGovernment(deliveryAddressSchema.getLocalGovernment()).build();
    }

    public static List<DeliveryAddress> convertListOfDeliveryAddressSchemaToListOfDeliveryAddressProto(List<DeliveryAddressSchema> deliveryAddressSchemaList){

        List<DeliveryAddress> deliveryAddressList = new ArrayList<>();
        for(DeliveryAddressSchema deliveryAddressSchema : deliveryAddressSchemaList){

            DeliveryAddress deliveryAddress = DeliveryAddress.newBuilder()
                    .setId(deliveryAddressSchema.getId())
                    .setCreatedAt(deliveryAddressSchema.getCreatedAt())
                    .setCreatedBy(deliveryAddressSchema.getCreatedBy())
                    .setState(deliveryAddressSchema.getState())
                    .setLocationAddress(deliveryAddressSchema.getLocationAddress())
                    .setLocalGovernment(deliveryAddressSchema.getLocalGovernment()).build();
            deliveryAddressList.add(deliveryAddress);
        }

        return deliveryAddressList;
    }

    public static DeliveryAddressSchema convertDeliveryAddressProtoToDeliveryAddressSchema(DeliveryAddress deliveryAddress) {
        DeliveryAddressSchema deliveryAddressSchema = DeliveryAddressSchema.builder()
                .state(deliveryAddress.getState())
                .locationAddress(deliveryAddress.getLocationAddress())
                .localGovernment(deliveryAddress.getLocalGovernment()).createdBy(deliveryAddress.getCreatedBy())
                .createdAt(deliveryAddress.getCreatedAt()).build();
        return deliveryAddressSchema;
    }
}
