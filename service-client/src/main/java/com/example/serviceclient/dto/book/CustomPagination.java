package com.example.serviceclient.dto.book;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CustomPagination {
    private long currentPage;
    private long perPage;
    private long totalItem;
    private long totalPage;

    private boolean hasPrevious;

    private boolean hasNext;
}