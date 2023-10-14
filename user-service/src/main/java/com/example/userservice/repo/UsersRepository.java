package com.example.userservice.repo;

import com.example.userservice.schema.UserSchema;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface UsersRepository extends MongoRepository<UserSchema, String> {

    @Query("{emailAddress:'?0'}")
    UserSchema findUserByEmailAddress(String emailAddress);

    @Query(value="{email:'?0'}", fields="{'firstName' : 1, 'lastName' : 1}")
    List<UserSchema> findAll(String email);

    public long count();

}
