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
	private StompMessageService messageService;
	
	@MessageMapping("/private")
	public void privateMessage(@Payload PrivateMessageDTO messagePayload) {
		PrivateMessageDTO modifiedMessage = 
			PrivateMessageDTO.builder()
				.toUserId(messagePayload.getFromUserId())
				.dateSent(messagePayload.getDateSent())
				.message(messagePayload.getMessage())
			.build();
		
		String toUserId = messagePayload.getToUserId();
		
		messageService.sendPrivateMessage(toUserId, modifiedMessage);
	}
	
	@MessageMapping("/group")
	public void groupMessage(@Payload GroupMessageDTO messagePayload) {
		String toGroupId = messagePayload.getToGroupId();
		
		messageService.sendGroupMessage(toGroupId, messagePayload);
	}
}
