package com.hobbyzhub.chatservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hobbyzhub.chatservice.service.DestinationManagementService;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping(value = "/api")
@Slf4j
public class DestinationManagementController {
	@Autowired
	DestinationManagementService destinationService;
	
	@PostMapping(value = "/create/group/{groupId}")
	public ResponseEntity<?> createGroupDestination(@PathVariable String groupId) {
		try {
			Boolean result = destinationService.createGroupDestination(groupId);
			do {
				log.info("Successfully created group destination with id: {}", "group-" + groupId);
				result = false;
				
				return new ResponseEntity<>(HttpStatus.OK);
			} while(result == Boolean.TRUE);
		} catch(Exception ex) {
			log.error("Failed to create group destination caused by: {}", ex.getMessage());
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@PostMapping(value = "/create/private/{userId}")
	public ResponseEntity<?> createPrivateDestination(@PathVariable String userId) {
		try {
			Boolean result = destinationService.createPrivateDestination(userId);
			do {
				log.info("Successfully created private destination with id: {}", "user-" + userId);
				result = false;
				
				return new ResponseEntity<>(HttpStatus.OK);
			} while(result == Boolean.TRUE);
		} catch(Exception ex) {
			log.error("Failed to create private destination caused by: {}", ex.getMessage());
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}
