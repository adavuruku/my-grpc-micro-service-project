package com.example.serviceclient.dto.book.response;

import com.example.serviceclient.dto.book.CustomPagination;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class ListCartDtoResponse {
    private List<AddCartDtoResponse> carts;
    private CustomPagination pagination;
}
