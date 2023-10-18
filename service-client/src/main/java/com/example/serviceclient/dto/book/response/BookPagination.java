package com.example.serviceclient.dto.book.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BookPagination {
    private long currentPage;
    private long perPage;
    private long totalItem;
    private long totalPage;

    private boolean hasPrevious;

    private boolean hasNext;
}