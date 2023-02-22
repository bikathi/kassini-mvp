package com.hobbyzhub.chatservice.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import com.hobbyzhub.chatservice.entity.UserChatsList;
import com.hobbyzhub.chatservice.payload.request.AddDelChatRequest;
import com.hobbyzhub.chatservice.repository.UserChatsListRepository;

@Service
public class UserChatsManagementService {
	@Autowired
	UserChatsListRepository chatsListRepository;
	
	@Autowired
	MongoTemplate mongoTemplate;
	
	public void createUserChatList(UserChatsList newList) {
		chatsListRepository.save(newList);
	}
	
	public void addToUserChatList(String userId, AddDelChatRequest newListItem) {
		Query query = new Query().addCriteria(Criteria.where("_id").is(userId));
		Update updateDefinition = new Update().push("chatList", newListItem);
		
		mongoTemplate.upsert(query, updateDefinition, UserChatsList.class);
	}
	
	public Optional<UserChatsList> getUserChatList(String userId) {
		return chatsListRepository.findById(userId);
	}
	
	public void deleteChatFromList(String userId, AddDelChatRequest chatToDelete) {
		Query query = new Query().addCriteria(Criteria.where("_id").is(userId));
		
		Update updateDefinition = new Update().pull("chatList", chatToDelete);
		mongoTemplate.findAndModify(query, updateDefinition, UserChatsList.class);
	}
	
	public void deleteEntireChatList(String userId) {
		chatsListRepository.deleteById(userId);
	}
}
