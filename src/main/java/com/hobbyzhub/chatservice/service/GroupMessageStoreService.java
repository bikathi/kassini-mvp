package com.hobbyzhub.chatservice.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import com.hobbyzhub.chatservice.entity.GroupMessageStore;
import com.hobbyzhub.chatservice.repository.GroupMessageStoreRepository;

@Service
public class GroupMessageStoreService {
	@Autowired
	GroupMessageStoreRepository storeRepository;
	
	@Autowired
	MongoTemplate mongoTemplate;
	
	public void saveNewChatStore(GroupMessageStore messageStore) {
		storeRepository.save(messageStore);
	}
	
	public boolean chatStoreExists(String chatId ) {
		return storeRepository.existsById(chatId);
	}
	
	public Optional<GroupMessageStore> getMessageStore(String chatId) {
		return storeRepository.findById(chatId);
	}
	
	public void updateChatStore(Query query, Update updateDefinition, Class<?> clazz) {
		mongoTemplate.upsert(query, updateDefinition, clazz);
	}
}
