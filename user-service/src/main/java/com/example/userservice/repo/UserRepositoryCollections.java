package com.example.userservice.repo;

import com.example.userservice.schema.UserSchema;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.FindOneAndUpdateOptions;
import com.mongodb.client.model.ReturnDocument;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

//https://www.baeldung.com/spring-data-mongodb-tutorial
@Repository
public class UserRepositoryCollections {

    private MongoTemplate mongoTemplate;
//    private MongoCollection<Document> mongoCollection =  mongoTemplate.getCollection("users");

//    public UserRepositoryCollections(){
//        this.mongoCollection =
//    }
    private MongoCollection<Document> prepareCollection(){
        return mongoTemplate.getCollection("users");
    }
    public Object updateUserRecord(String updateFields, String userName){
        MongoCollection<Document> mongoCollection = prepareCollection();
        FindOneAndUpdateOptions options = new FindOneAndUpdateOptions();
        options.returnDocument(ReturnDocument.AFTER);
        Document find = new Document("emailAddress",userName);
        Document parse = Document.parse(updateFields);
        Document update = new Document("$set",parse );
        return mongoCollection.findOneAndUpdate(find, update, options);
    }
}
