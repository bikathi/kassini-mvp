package com.hobbyzhub.chatservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Controller;

import com.hobbyzhub.chatservice.dto.GroupMessageDTO;
import com.hobbyzhub.chatservice.dto.PrivateMessageDTO;
import com.hobbyzhub.chatservice.service.StompMessageService;

import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
public class StompMessageController {
	@Autowired
	StompMessageService stompMessageService;
	
	@MessageMapping("/private")
	public void privateMessage(@Payload PrivateMessageDTO messagePayload) {
		
		String toUserId = messagePayload.getToUserId();
		
		Boolean result = stompMessageService.sendPrivateMessage(toUserId, messagePayload);
		if(result == Boolean.TRUE) {
			log.info(
				"Successfully sent private message fromUserId: {} toUserId: {}", 
				messagePayload.getFromUserId(),
				messagePayload.getToUserId()
			);
		} else {
			log.error(
				"Failed to send private message fromUserId: {} toUserId: {}",
				messagePayload.getFromUserId(),
				messagePayload.getToUserId()
			);
		}
	}
	
	@MessageMapping("/group")
	public void groupMessage(@Payload GroupMessageDTO messagePayload) {
		String toGroupId = messagePayload.getToGroupId();
		
		boolean result = stompMessageService.sendGroupMessage(toGroupId, messagePayload);
		if(result == Boolean.TRUE) {
			log.info(
				"Successfully sent group message fromUserId: {} toGroupId: {}", 
				messagePayload.getFromUserId(),
				messagePayload.getToGroupId()
			);
		} else {
			log.error(
				"Failed to send group message fromUserId: {} toGroupId: {}",
				messagePayload.getFromUserId(),
				messagePayload.getToGroupId()
			);
		}
	}
}
