package com.hobbyzhub.chatservice.service;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;

import com.hobbyzhub.chatservice.dto.GroupMessageDTO;
import com.hobbyzhub.chatservice.dto.PrivateMessageDTO;
import com.hobbyzhub.chatservice.entity.PrivateMessageStore;

public class MessageConvenienceMethods {
	@Autowired
	static PrivateMessageStoreService privateMessageStoreService;
	
	public static void addPrivateMessageToStore(PrivateMessageDTO message) {
		// then save the message to the chat store
        String chatId = message.getFromUserId() + message.getToUserId();
        // if the chat store already exists, just add this message to the list of messages
        // and update the message count
        if(privateMessageStoreService.chatStoreExists(chatId) == (Boolean.TRUE)) {
        	PrivateMessageStore tempChatStore = privateMessageStoreService.getMessageStore(chatId).get();
        	
        	tempChatStore.addMessage(
    			PrivateMessageDTO.builder()
    			.message(message.getMessage())
    			.dateSent(message.getDateSent())
    			.build()
			);
        	
        	// increase message count
        	tempChatStore.increaseMessageCount();
        	
        	privateMessageStoreService.updateChatStore(tempChatStore);
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
	
	public static void addGroupMessageToStore(String chatId, GroupMessageDTO message) {
		
	}
}
