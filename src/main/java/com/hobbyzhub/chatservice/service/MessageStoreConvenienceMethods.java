package com.hobbyzhub.chatservice.service;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import com.hobbyzhub.chatservice.dto.GroupMessageDTO;
import com.hobbyzhub.chatservice.dto.PrivateMessageDTO;
import com.hobbyzhub.chatservice.entity.GroupMessageStore;
import com.hobbyzhub.chatservice.entity.PrivateMessageStore;

@Service
public class MessageConvenienceMethods {
	@Autowired
	PrivateMessageStoreService privateMessageStoreService;
	
	@Autowired
	GroupMessageStoreService groupMessageStoreService;
	
	public void addPrivateMessageToStore(PrivateMessageDTO message) {
        String chatId = message.getFromUserId() + message.getToUserId();
        
        // if the chat store already exists, just add this message to the list of messages
        // and update the message count
        if(privateMessageStoreService.chatStoreExists(chatId) == (Boolean.TRUE)) {
        	Query query = new Query().addCriteria(Criteria.where("_id").is(chatId));
        	
        	PrivateMessageDTO messageToStore = 
    			PrivateMessageDTO.builder()
    			.message(message.getMessage())
    			.dateSent(message.getDateSent())
    			.build();
        	Update updateDefinition = new Update().push("messages", messageToStore);
        	
        	privateMessageStoreService.updateChatStore(query, updateDefinition, PrivateMessageStore.class);
    	// else if the chat store does not exists(the chat is therefore new), create a new store
    	// and add this message to the store
        } else {
        	PrivateMessageStore newMessageStore = 
    			PrivateMessageStore.builder()
    			.chatId(chatId)
    			.dateInitiated(LocalDate.now().toString())
    			.messageCount(1L)
    			.build();
        	
        	newMessageStore.addMessage(
    			PrivateMessageDTO.builder()
    			.message(message.getMessage())
    			.dateSent(message.getDateSent())
    			.build()
			);
        	
        	privateMessageStoreService.saveNewChatStore(newMessageStore);
        }
	}
	
	public void addGroupMessageToStore(GroupMessageDTO message) {
		// the chatId is similar to the groupId
		String chatId = message.getToGroupId();
		
		if(groupMessageStoreService.chatStoreExists(chatId) == Boolean.TRUE) {
			Query query = new Query().addCriteria(Criteria.where("_id").is(chatId));
			
			// structure of the message we're storing drops the toGroupId variable
			GroupMessageDTO messageToStore = GroupMessageDTO.builder()
				.fromUserId(message.getFromUserId())
				.message(message.getMessage())
				.dateSent(message.getDateSent())
				.build();
			Update updateDefinition = new Update().push("messages", messageToStore);
			
			groupMessageStoreService.updateChatStore(query, updateDefinition, GroupMessageStore.class);
		// else if the chat store does not exist(the chat is therefore new), create a new store
    	// and add this message to the store
		} else {
			GroupMessageStore newMessageStore = 
				GroupMessageStore.builder()
				.chatId(chatId)
				.dateInitiated(LocalDate.now().toString())
				.messageCount(1L)
				.build();
			
			newMessageStore.addMessage(
				GroupMessageDTO.builder()
				.fromUserId(message.getFromUserId())
				.message(message.getMessage())
				.dateSent(message.getDateSent())
				.build()
			);
			
			groupMessageStoreService.saveNewChatStore(newMessageStore);
		}
	}
}
