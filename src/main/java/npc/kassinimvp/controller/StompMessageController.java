package npc.kassinimvp.controller;

import npc.kassinimvp.payload.request.ProductChatMessagePayload;
import npc.kassinimvp.payload.request.TextChatMessagePayload;
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
	
	@MessageMapping("/private-text")
	public void privateTextMessage(@Payload TextChatMessagePayload messagePayload) {
		String toUserId = messagePayload.getToUserId();
		boolean result = stompMessageService.sendPrivateTextMessage(toUserId, messagePayload);
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

	@MessageMapping("/private-product")
	public void privateProductMessage(@Payload ProductChatMessagePayload messagePayload) {
		String toUserId = messagePayload.getToUserId();
		boolean result = stompMessageService.sendPrivateProductMessage(toUserId, messagePayload);
		if(!result) {
			log.error(
				"Failed to send private product message fromUserId: {} toUserId: {}",
				messagePayload.getFromUserId(),
				messagePayload.getToUserId()
			);
		}

		log.info(
			"Successfully sent private product message fromUserId: {} toUserId: {}",
			messagePayload.getFromUserId(),
			messagePayload.getToUserId()
		);
	}
}
