package com.example.bookservice.schema;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.annotation.Id;

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
}
