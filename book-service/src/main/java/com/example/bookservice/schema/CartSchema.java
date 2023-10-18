package com.example.bookservice.schema;

import com.example.book_service.Book;
import com.example.book_service.BookImage;
import com.example.book_service.Cart;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;

import java.util.ArrayList;
import java.util.List;

@Data
@Document("carts")
@Builder
//@NoArgsConstructor
public class CartSchema {
    @Id
    private String id;
    @DocumentReference
    private BookSchema book;
    private String createdAt;
    private String createdBy;
    private Long quantity;

    public static List<Cart> convertListOfCartSchemaToListOfCartProto(List<CartSchema> cartSchemaList){
        List<Cart> cartList = new ArrayList<>();
        for(CartSchema cartSchemaDto : cartSchemaList){

            Book book = BookSchema.convertBookSchemaToBookProto(cartSchemaDto.getBook());

            Cart cartItem = Cart.newBuilder()
                    .setQuantity(cartSchemaDto.getQuantity())
                    .setCreatedBy(cartSchemaDto.getCreatedBy())
                    .setCreatedAt(cartSchemaDto.getCreatedAt())
                    .setBook(book)
                    .setId(cartSchemaDto.getId()).build();
            cartList.add(cartItem);
        }

        return cartList;
    }
}
