package com.example.paymentprocessor.schema;

import lombok.Builder;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
public class BookOrder {
    private String bookId;
    private String title;
    private double price;
    private long quantity;
    private double discount;
    private String isbn;
    private double amount;

    public static List<BookOrder> convertListOfBookOrderProtoToBookOrderPojo(List<com.example.payment_service.BookOrder> bookOrdersList) {
        List<BookOrder> bookOrderList = new ArrayList<>();
        for(com.example.payment_service.BookOrder bookOrder : bookOrdersList){
            bookOrderList.add(
                    BookOrder.builder()
                            .amount(bookOrder.getAmount())
                            .bookId(bookOrder.getBookId())
                            .price(bookOrder.getPrice())
                            .isbn(bookOrder.getIsbn())
                            .discount(bookOrder.getDiscount())
                            .quantity(bookOrder.getQuantity())
                            .title(bookOrder.getTitle()).build()

            );
        }
        return bookOrderList;
    }

    public static List<com.example.payment_service.BookOrder> convertListOfBookOrderPojoToBookOrderProto(List<BookOrder> bookOrders) {
        List<com.example.payment_service.BookOrder> bookOrderList = new ArrayList<>();
        for(BookOrder bookOrder: bookOrders){
            bookOrderList.add(
                    com.example.payment_service.BookOrder.newBuilder()
                            .setAmount(bookOrder.getAmount())
                            .setBookId(bookOrder.bookId)
                            .setDiscount(bookOrder.getDiscount())
                            .setPrice(bookOrder.getPrice())
                            .setIsbn(bookOrder.getIsbn())
                            .setQuantity(bookOrder.getQuantity())
                            .setTitle(bookOrder.getTitle()).build()
            );
        }
        return bookOrderList;
    }
}
