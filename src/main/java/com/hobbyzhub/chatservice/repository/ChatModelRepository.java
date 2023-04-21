package com.hobbyzhub.chatservice.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatModelRepository extends MongoRepository<com.hobbyzhub.chatservice.entity.ChatModel, String> {
}
