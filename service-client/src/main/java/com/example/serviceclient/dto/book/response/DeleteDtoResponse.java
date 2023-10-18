package com.example.serviceclient.dto.book.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DeleteDtoResponse {
    private String id;
    private String statusMessage;
}
