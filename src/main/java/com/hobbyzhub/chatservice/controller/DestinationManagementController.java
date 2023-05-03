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
			boolean result = destinationService.createGroupDestination(groupId);
			if(!result) {
				log.error("Failed to create group destination");
				return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
			}

			log.info("Successfully created group destination with id: {}", "group-" + groupId);
			return new ResponseEntity<>(HttpStatus.OK);
		} catch(Exception ex) {
			log.error("Failed to create group destination caused by: {}", ex.getMessage());
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@PostMapping(value = "/create/private/{userId}")
	public ResponseEntity<?> createPrivateDestination(@PathVariable String userId) {
		try {
			boolean result = destinationService.createPrivateDestination(userId);
			if(!result) {
				log.error("Failed to create private destination");
				return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
			}

			log.info("Successfully created private destination with id: {}", "user-" + userId);
			return new ResponseEntity<>(HttpStatus.OK);
		} catch(Exception ex) {
			log.error("Failed to create private destination caused by: {}", ex.getMessage());
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}
