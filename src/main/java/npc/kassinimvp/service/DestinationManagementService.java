package npc.kassinimvp.service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import npc.kassinimvp.entity.ChatMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;
import npc.kassinimvp.payload.request.TextChatMessagePayload;

import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.jms.TextMessage;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class DestinationManagementService {
	@Autowired
	JmsTemplate jmsTemplate;

	@Autowired
	ChatMessageService chatMessageService;
	
	public boolean createPrivateDestination(String userId) {
		TextChatMessagePayload internalPrivateMessage =
			TextChatMessagePayload.builder()
			.chatId("kassiniUUID")
			.fromUserId("Kassini")
			.toUserId(userId)
			.message("Welcome to Kassini. We're thrilled to have you.")
			.dateSent(LocalDate.now().format(DateTimeFormatter.ofPattern("E MMM d yyyy"))) // example Fri Aug 11 2023
		.build();
		
		try {
			String internalMessage = new ObjectMapper().writer().withDefaultPrettyPrinter()
				.writeValueAsString(internalPrivateMessage);
			
			jmsTemplate.send("user-" + userId, messageCreator -> {
                TextMessage deliverable = messageCreator.createTextMessage();
                deliverable.setText(internalMessage);
                return deliverable;
            });

			// save message to DB
			ChatMessage messageToSave = ChatMessage.builder()
				.messageId(internalPrivateMessage.getChatId())
				.typeOfMessage("text")
				.chatId(internalPrivateMessage.getChatId())
				.message(internalPrivateMessage.getMessage())
				.fromUserId(internalPrivateMessage.getFromUserId())
				.toUserId(userId)
				.dateSent(internalPrivateMessage.getDateSent())
				.productTextDetails(null)
			.build();

			chatMessageService.saveChat(messageToSave);

			log.info("JMSTemplate created new destination of ID: {}", "user-" + userId);
			return true;
		} catch(Exception ex) {
			log.error("JMSTemplate error creating new destination: {}", ex.getMessage());
			return false;
		}
	}
}
