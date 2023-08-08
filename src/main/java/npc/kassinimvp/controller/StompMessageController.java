package npc.kassinimvp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Controller;

import npc.kassinimvp.service.StompMessageService;

import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
public class StompMessageController {
	@Autowired
	StompMessageService stompMessageService;
	
	@MessageMapping("/private")
	public void privateMessage(@Payload ChatMessagePayload messagePayload) {
		
		String toUserId = messagePayload.getToUserId();
		
		boolean result = stompMessageService.sendPrivateMessage(toUserId, messagePayload);
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
}
