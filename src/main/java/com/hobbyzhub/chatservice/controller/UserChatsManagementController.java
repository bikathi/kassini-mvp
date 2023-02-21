package com.hobbyzhub.chatservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
		return null;
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
