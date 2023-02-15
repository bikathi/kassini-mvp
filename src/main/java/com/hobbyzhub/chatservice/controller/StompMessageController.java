package com.hobbyzhub.chatservice.controller;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Controller;

import com.hobbyzhub.chatservice.dto.GroupMessageDTO;
import com.hobbyzhub.chatservice.dto.PrivateMessageDTO;

import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
public class StompMessageController {
	@MessageMapping("/private")
	public void privateMessage(@Payload PrivateMessageDTO messagePayload) {
		
	}
	
	@MessageMapping("/group")
	public void groupMessage(@Payload GroupMessageDTO messagePayload) {
		
	}
}
