package com.example.bookservice.schema;

import com.example.book_service.BookImage;
import lombok.Builder;
import lombok.Data;
import org.apache.commons.beanutils.BeanUtils;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.annotation.Id;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@Document
public class BookImageSchema {
    @Id
    private String id;
    private String format;
    private String resourceType;
    private String secureUrl;
    private String createdAt;
    private String url;
    private Long bytes;
    private Long width;
    private Long height;

    public static List<BookImageSchema> createListOfBookingImage(List<BookImage> bookImageList){
        List<BookImageSchema> bookImageSchemaList = new ArrayList<>();
        for(BookImage bookImage : bookImageList){
            BookImageSchema bookImageSchema = BookImageSchema.builder().build();
            try {
                BeanUtils.copyProperties(bookImageSchema, bookImage);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            } catch (InvocationTargetException e) {
                throw new RuntimeException(e);
            }
            bookImageSchemaList.add(bookImageSchema);
        }
        return bookImageSchemaList;
    }
}
