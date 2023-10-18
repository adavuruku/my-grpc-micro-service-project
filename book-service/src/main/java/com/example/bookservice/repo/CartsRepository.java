package com.example.bookservice.repo;

import com.example.bookservice.schema.BookSchema;
import com.example.bookservice.schema.CartSchema;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface CartsRepository extends MongoRepository<CartSchema, String> {

    Optional<CartSchema> findByIdAndCreatedBy(String id, String createdBy);
    Page<CartSchema> findByCreatedBy(String createdBy, Pageable pageable);
    public long count();

}
