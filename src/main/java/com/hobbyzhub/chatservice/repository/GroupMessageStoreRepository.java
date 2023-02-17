package com.hobbyzhub.chatservice.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.hobbyzhub.chatservice.entity.GroupMessageStore;

@Repository
public interface GroupMessageStoreRepository 
	extends MongoRepository<GroupMessageStore, String> {

}
