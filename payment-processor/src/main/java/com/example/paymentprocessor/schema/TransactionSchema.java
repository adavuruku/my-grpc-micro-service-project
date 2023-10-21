package com.example.paymentprocessor.schema;

import com.example.book_service.Book;
import com.example.book_service.Cart;
import com.example.payment_service.DeliveryAddress;
import com.example.payment_service.PAYMENT_METHOD;
import com.example.payment_service.Transaction;
import com.example.payment_service.TransactionResponse;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;
import org.springframework.data.mongodb.core.index.Indexed;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@Document("transactions")
@Builder
//@NoArgsConstructor
public class TransactionSchema {
    @Id
    private String id;
    @Indexed(unique = true)
    private String transactionRef;
    @DocumentReference
    private DeliveryAddressSchema deliveryAddressSchema;
    private List<BookOrder> bookOrders;
    private String createdByName;
    private String createdAt;
    private String createdBy;
    private String paymentMethod; // CASH, POS, TRANSFER
    private String paymentOption; // PAY_ON_DELIVERY, PAY_BEFORE_DELIVERY
    private double discountOnGrandTotal;
    private double otherCostCharges;
    private double grandTotal;
    private boolean deliveryStatus;
    private boolean isReceiptPaid;

    public static TransactionSchema convertTransactionProtoToTransactionSchema(Transaction transaction) {
        List<BookOrder> bookOrderList = BookOrder.convertListOfBookOrderProtoToBookOrderPojo(transaction.getBookOrdersList());
        return TransactionSchema.builder()
                .id(transaction.getId())
                .bookOrders(bookOrderList)
                .transactionRef(transaction.getTransactionRef())
                .createdAt(new Date().toString())
                .createdBy(transaction.getCreatedBy()).createdByName(transaction.getCreatedByName())
                .paymentMethod(transaction.getPaymentMethod())
                .paymentOption(transaction.getPaymentOption())
                .discountOnGrandTotal(transaction.getDiscountOnGrandTotal()).otherCostCharges(transaction.getOtherCostCharges())
                .grandTotal(transaction.getGrandTotal()).deliveryStatus(transaction.getDeliveryStatus())
                .isReceiptPaid(transaction.getIsReceiptPaid()).build();
    }

    public static TransactionResponse convertTransactionSchemaToTransactionProto(TransactionSchema transactionSchemaData) {
        List<com.example.payment_service.BookOrder> bookOrderList =
                BookOrder.convertListOfBookOrderPojoToBookOrderProto(transactionSchemaData.getBookOrders());
        DeliveryAddress deliveryAddress = DeliveryAddressSchema.convertDeliveryAddressSchemaToDeliveryAddressProto(transactionSchemaData.getDeliveryAddressSchema());

        return TransactionResponse.newBuilder()
                .setId(transactionSchemaData.getId())
                .addAllBookOrders(bookOrderList)
                .setTransactionRef(transactionSchemaData.getTransactionRef())
                .setCreatedAt(transactionSchemaData.getCreatedAt())
                .setCreatedBy(transactionSchemaData.getCreatedBy())
                .setCreatedByName(transactionSchemaData.getCreatedByName())
                .setPaymentMethod(transactionSchemaData.getPaymentMethod())
                .setPaymentOption(transactionSchemaData.getPaymentOption())
                .setDiscountOnGrandTotal(transactionSchemaData.getDiscountOnGrandTotal())
                .setGrandTotal(transactionSchemaData.getGrandTotal())
                .setOtherCostCharges(transactionSchemaData.getOtherCostCharges())
                .setDeliveryStatus(transactionSchemaData.isDeliveryStatus())
                .setIsReceiptPaid(transactionSchemaData.isReceiptPaid())
                .setDeliveryAddress(deliveryAddress).build();
    }


//    transactionRef:
//    createdBy:
//    createdByName:
//    books [Bookid, title, price, qty, discount,isbn]
//    discountOnGrandTotal:[]
//    otherCostCharges:
//    deliveryAddress:
//    paymentMethod: [CASH, POS, TRANSFER]
//    paymentOption: [PAY_ON_DELIVERY, PAY_BEFORE_DELIVERY]
//    grandTotal:
//    createdAt:
//    deliveryStatus:[True/False]
//    isReceiptPaid:[True/False]

//    public static List<Cart> convertListOfCartSchemaToListOfCartProto(List<TransactionSchema> cartSchemaList){
//        List<Cart> cartList = new ArrayList<>();
//        for(TransactionSchema cartSchemaDto : cartSchemaList){
//
//            Book book = DeliveryAddressSchema.convertBookSchemaToBookProto(cartSchemaDto.getBook());
//
//            Cart cartItem = Cart.newBuilder()
//                    .setQuantity(cartSchemaDto.getQuantity())
//                    .setCreatedBy(cartSchemaDto.getCreatedBy())
//                    .setCreatedAt(cartSchemaDto.getCreatedAt())
//                    .setBook(book)
//                    .setId(cartSchemaDto.getId()).build();
//            cartList.add(cartItem);
//        }
//
//        return cartList;
//    }
}
