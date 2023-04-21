package com.hobbyzhub.chatservice.repository;

import com.hobbyzhub.chatservice.entity.MessageModel;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MessageModelRepository extends MongoRepository<MessageModel, String> {
}
