package com.hobbyzhub.chatservice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hobbyzhub.chatservice.repository.UserChatsListRepository;

@Service
public class UserChatsManagementService {
	@Autowired
	UserChatsListRepository chatsListRepository;
	
	public void createUserChatList(String userId) {
		
	}
	
	public void updateUserChatList(String userId) {
		
	}
	
	public void getUserChatList(String userId) {
		
	}
	
	public void deleteChatFromList(String userId) {
		
	}
	
	public void deleteEntireChatList(String userId) {
		
	}
}
