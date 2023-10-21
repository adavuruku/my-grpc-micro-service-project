package com.example.paymentprocessor.repo;

import com.example.paymentprocessor.schema.TransactionSchema;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface TransactionRepo extends MongoRepository<TransactionSchema, String> {

    List<TransactionSchema> findAll();
//    Page<TransactionSchema> findByisDeleted(boolean isDeleted, Pageable pageable);

//    Optional<TransactionSchema> findByBookSlug(String bookSlug);
    Optional<TransactionSchema> findById(String id);
//    Optional<TransactionSchema> findByIdOrBookSlug(String id, String bookSlug);
//    Optional<TransactionSchema> findByIdAndIsDeleted(String id, boolean isDeleted);
//    Optional<TransactionSchema> findByIdAndIsDeletedAndCreatedBy(String id, boolean isDeleted,String createdBy);


    public long count();

    Optional<TransactionSchema> findByIdAndTransactionRef(String id, String transactionRef);

    Page<TransactionSchema> findByCreatedBy(String userName, Pageable paging);

    Optional<TransactionSchema> findByCreatedByAndTransactionRef(String userName, String transactionReference);
}
