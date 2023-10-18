package com.example.bookservice.schema;

import com.example.book_service.BookImage;
import lombok.Builder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.beanutils.BeanUtils;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.annotation.Id;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
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

    public static List<BookImageSchema> createListOfBookingImageSchemaFromBookImageProto(List<BookImage> bookImageList){
        List<BookImageSchema> bookImageSchemaList = new ArrayList<>();
        for(BookImage bookImage : bookImageList){
            BookImageSchema bookImageSchema = BookImageSchema.builder()
                    .format(bookImage.getFormat()).url(bookImage.getUrl())
                    .width(bookImage.getWidth()).resourceType(bookImage.getResourceType())
                    .createdAt(bookImage.getCreatedAt()).bytes(bookImage.getBytes())
                    .height(bookImage.getHeight()).secureUrl(bookImage.getSecureUrl()).build();
            bookImageSchemaList.add(bookImageSchema);
        }
        return bookImageSchemaList;
    }

    public static List<BookImage> createListOfBookImageProtoFromBookingImageSchema(List<BookImageSchema> bookImageSchemaList){
        List<BookImage> bookImageList = new ArrayList<>();
        for(BookImageSchema bookImageSchema : bookImageSchemaList){
            BookImage bookImage = BookImage.newBuilder()
                    .setFormat(bookImageSchema.getFormat()).setUrl(bookImageSchema.getUrl())
                    .setWidth(bookImageSchema.getWidth()).setResourceType(bookImageSchema.getResourceType())
                    .setCreatedAt(bookImageSchema.getCreatedAt()).setBytes(bookImageSchema.getBytes())
                    .setHeight(bookImageSchema.getHeight()).setSecureUrl(bookImageSchema.getSecureUrl()).build();
            bookImageList.add(bookImage);
        }
        return bookImageList;
    }
}
