package com.example.paymentprocessor.repo;

import com.example.paymentprocessor.schema.DeliveryAddressSchema;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface DeliveryAddressRepo extends MongoRepository<DeliveryAddressSchema, String> {

    Optional<DeliveryAddressSchema> findByIdAndCreatedBy(String id, String createdBy);
    Page<DeliveryAddressSchema> findByCreatedBy(String createdBy, Pageable pageable);
    public long count();

}
