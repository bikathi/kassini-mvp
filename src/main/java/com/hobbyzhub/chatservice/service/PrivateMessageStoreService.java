package com.hobbyzhub.chatservice.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hobbyzhub.chatservice.entity.PrivateMessageStore;
import com.hobbyzhub.chatservice.repository.PrivateMessageStoreRepository;

@Service
public class PrivateMessageStoreService {
	@Autowired
	PrivateMessageStoreRepository storeRepository;
	
	public void saveNewChatStore(PrivateMessageStore messageStore) {
		storeRepository.save(messageStore);
	}
	
	public boolean chatStoreExists(String chatId) {
		return storeRepository.existsById(chatId);
	}
	
	public Optional<PrivateMessageStore> getMessageStore(String chatId) {
		return storeRepository.findById(chatId);
	}
	
	public void updateChatStore(PrivateMessageStore updatedStore) {
		storeRepository.save(null);
	}
}
