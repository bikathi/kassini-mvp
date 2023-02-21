package com.hobbyzhub.chatservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hobbyzhub.chatservice.entity.UserChatsList;
import com.hobbyzhub.chatservice.service.UserChatsManagementService;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping(value = "/api")
@Slf4j
public class UserChatsManagementController {
	@Autowired
	UserChatsManagementService chatsManagementService;
	
	@PostMapping(value = "/create/list/{userId}")
	public ResponseEntity<?> createUserChatList(@PathVariable String userId) {
		UserChatsList newList = 
			UserChatsList.builder()
			.userId(userId)
			.build();
		
		try {
			chatsManagementService.createUserChatList(newList);
			
			log.info("Created new user chats list of id: {}", userId);
			return new ResponseEntity<>(HttpStatus.CREATED);
		} catch(Exception ex) {
			log.error("Error while creating user chat list: {}", ex.getMessage());
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@PutMapping(value = "/update/list/{userId}")
	public ResponseEntity<?> updateUserChatList(@PathVariable String userId) {
		return null;
	}
	
	@GetMapping(value = "/get/list/userId")
	public ResponseEntity<?> getUserChatList(@PathVariable String ChatId) {
		return null;
	}
	
	@DeleteMapping(value = "/delete/{userId}/{chatId}")
	public ResponseEntity<?> deleteChatFromList(@PathVariable String userId, @PathVariable String chatId) {
		return null;
	}
	
	@DeleteMapping(value = "/delete/list/{userId}")
	public ResponseEntity<?> deleteEntireChatList(@PathVariable String userId) {
		return null;
	}
}
