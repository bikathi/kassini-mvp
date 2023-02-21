package com.hobbyzhub.chatservice.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.hobbyzhub.chatservice.entity.UserChatsList;

@Repository
public interface UserChatsListRepository 
	extends MongoRepository<UserChatsList, String> {

}
