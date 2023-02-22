package com.hobbyzhub.chatservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hobbyzhub.chatservice.entity.UserChatsList;
import com.hobbyzhub.chatservice.payload.request.AddDelChatRequest;
import com.hobbyzhub.chatservice.payload.response.GenericServiceResponse;
import com.hobbyzhub.chatservice.service.UserChatsManagementService;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping(value = "/api")
@Slf4j
public class UserChatsManagementController {
	@Autowired
	UserChatsManagementService chatsManagementService;
	
	@Value("${service.api.version}")
	private String apiVersion;
	
	@Value("${service.organization.name}")
	private String organizationName;
	
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
	public ResponseEntity<?> addToUserChatList(
		@PathVariable String userId, @RequestBody AddDelChatRequest newListItem) {
		try {
			chatsManagementService.addToUserChatList(userId, newListItem);
			
			log.info("Successfully add new chat to user's list");
			return new ResponseEntity<>(HttpStatus.OK);
		} catch(Exception ex) {
			log.error("Error while adding new chat to user's list: {}", ex.getMessage());
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@GetMapping(value = "/get/list/{userId}")
	public ResponseEntity<?> getUserChatList(@PathVariable String userId) {
		try {
			UserChatsList retrievedList = chatsManagementService.getUserChatList(userId).get();
			return new ResponseEntity<>(
				new GenericServiceResponse<UserChatsList>(
					apiVersion, 
					organizationName, 
					"Successfully retrieved chats list for userId: " + userId, 
					HttpStatus.OK.value(), 
					retrievedList), 
			HttpStatus.OK);
		} catch(Exception ex) {
			log.error("Error while retrieving chats list for userID: {}", ex.getMessage());
			return new ResponseEntity<>(
				new GenericServiceResponse<>(
					apiVersion, 
					organizationName, 
					"Error while retrieving chats list for userId: " + userId, 
					HttpStatus.INTERNAL_SERVER_ERROR.value(), 
					null), 
			HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@DeleteMapping(value = "/delete/chat/{userId}")
	public ResponseEntity<?> deleteChatFromList(
		@PathVariable String userId, @RequestBody AddDelChatRequest deleteRequest) {
		try {
			chatsManagementService.deleteChatFromList(userId, deleteRequest);
			
			log.info("Successfully delete chat from list of userId: {}", userId);
			return new ResponseEntity<>(
				new GenericServiceResponse<UserChatsList>(
					apiVersion, 
					organizationName, 
					"Successfully delete chat from chats list of userId: " + userId, 
					HttpStatus.OK.value(), 
					null), 
			HttpStatus.OK);
		} catch(Exception ex) {
			log.error("Error deleting chat with ID: {} from user's list of chats", deleteRequest.getChatId());
			return new ResponseEntity<>(
				new GenericServiceResponse<>(
					apiVersion, 
					organizationName, 
					"Error deleting chat with ID: " + deleteRequest.getChatId() + " from user's list of chats", 
					HttpStatus.INTERNAL_SERVER_ERROR.value(), 
					null), 
			HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@DeleteMapping(value = "/delete/list/{userId}")
	public ResponseEntity<?> deleteEntireChatList(@PathVariable String userId) {
		try {
			chatsManagementService.deleteEntireChatList(userId);
			
			log.info("Successfully deleted entire chat list for userId: {}", userId);
			return new ResponseEntity<>(HttpStatus.OK);
		} catch(Exception ex) {
			log.error("Error while trying to delete entire chat list: {}", ex.getMessage());
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}
