package com.hobbyzhub.chatservice.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.hobbyzhub.chatservice.entity.PrivateMessageStore;

@Repository
public interface PrivateMessageStoreRepository 
	extends MongoRepository<PrivateMessageStore, String> {

}
