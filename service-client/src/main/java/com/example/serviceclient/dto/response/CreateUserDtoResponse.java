package com.example.serviceclient.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CreateUserDtoResponse {
    private String id;
    private String statusMessage;
}
