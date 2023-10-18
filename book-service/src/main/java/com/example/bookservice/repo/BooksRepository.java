package com.example.bookservice.repo;

import com.example.bookservice.schema.BookSchema;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;
import java.util.Optional;

public interface BooksRepository extends MongoRepository<BookSchema, String> {

    List<BookSchema> findAll();
    Page<BookSchema> findByisDeleted(boolean isDeleted, Pageable pageable);

    Optional<BookSchema> findByBookSlug(String bookSlug);
    Optional<BookSchema> findById(String id);
    Optional<BookSchema> findByIdOrBookSlug(String id, String bookSlug);

    public long count();

}
