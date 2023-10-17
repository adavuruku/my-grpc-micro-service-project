package com.example.userservice.repo;

import com.example.userservice.schema.UserSchema;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.FindOneAndUpdateOptions;
import com.mongodb.client.model.ReturnDocument;
import com.mongodb.client.result.UpdateResult;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

//https://www.baeldung.com/spring-data-mongodb-tutorial
@Repository
public class UserRepositoryTemplate {
    @Autowired
    private MongoTemplate mongoTemplate;
    public Long updateUserRecord(Object findFields, String userName){
        FindOneAndUpdateOptions options = new FindOneAndUpdateOptions();
        options.returnDocument(ReturnDocument.AFTER);

        Query query = new Query();
        query.addCriteria(Criteria.where("emailAddress").is(userName));

        Update update = new Update();
        update.set("firstName", "Akhi");
        update.set("lastName", "Akhi");

        UpdateResult updateResult =  mongoTemplate.upsert(query, update, UserSchema.class);
        return updateResult.getMatchedCount();
    }
}
