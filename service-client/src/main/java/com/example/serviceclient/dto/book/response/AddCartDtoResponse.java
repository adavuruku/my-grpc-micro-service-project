package com.example.serviceclient.dto.book.response;

import com.example.book_service.Book;
import com.example.book_service.Cart;
import com.example.serviceclient.dto.FileResponse;
import lombok.Builder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@Slf4j
public class AddCartDtoResponse {
    private String id;
    private CreateBookDtoResponse book;
    private String createdBy;
    private String createdAt;
    private Long quantity;

    public static AddCartDtoResponse fromProtoToObject(Cart cart){
        CreateBookDtoResponse createBookDtoResponse = CreateBookDtoResponse.fromProtoToObject(cart.getBook());

        return AddCartDtoResponse.builder()
                .id(cart.getId())
                .book(createBookDtoResponse)
                .quantity(cart.getQuantity())
                .createdAt(cart.getCreatedAt())
                .createdBy(cart.getCreatedBy()).build();
    }

    public static List<AddCartDtoResponse> fromListOfCartResponseProtoToPojo(List<Cart> cartList){
        List<AddCartDtoResponse> listOfCardResponse = new ArrayList<>();

        for(Cart cart : cartList){
            CreateBookDtoResponse createBookDtoResponse = CreateBookDtoResponse.fromProtoToObject(cart.getBook());
            AddCartDtoResponse addCartDtoResponse = AddCartDtoResponse.builder()
                    .id(cart.getId())
                    .book(createBookDtoResponse)
                    .quantity(cart.getQuantity())
                    .createdAt(cart.getCreatedAt())
                    .createdBy(cart.getCreatedBy()).build();

            listOfCardResponse.add(addCartDtoResponse);

        }
        return listOfCardResponse;
    }
}