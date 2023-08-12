package npc.kassinimvp.service;

import npc.kassinimvp.entity.ChatMessage;
import npc.kassinimvp.payload.request.ProductChatMessagePayload;
import org.jgroups.demos.Chat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;
import npc.kassinimvp.payload.request.TextChatMessagePayload;

import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.jms.TextMessage;
import lombok.extern.slf4j.Slf4j;

import java.util.UUID;

@Service
@Slf4j
public class StompMessageService {
	@Autowired
	JmsTemplate jmsTemplate;

    @Autowired
    ChatMessageService chatMessageService;
	
	public boolean sendPrivateTextMessage(String toUserId, TextChatMessagePayload message) {
		try {
            String jsonObj = new ObjectMapper().writer().withDefaultPrettyPrinter().writeValueAsString(message);
            // send the message to the queue
            jmsTemplate.send(String.format("user-%s", toUserId), messageCreator -> {
                TextMessage deliverable = messageCreator.createTextMessage();
                deliverable.setText(jsonObj);
                return deliverable;
            });

            // then save it into the database
            ChatMessage messageToSave = ChatMessage.builder()
                .messageId(generateChatMessageId())
                .typeOfMessage("text")
                .chatId(message.getChatId())
                .message(message.getMessage())
                .fromUserId(message.getFromUserId())
                .toUserId(message.getToUserId())
                .dateSent(message.getDateSent())
                .productTextDetails(null)
            .build();
            chatMessageService.saveChat(messageToSave);

            return true;
        } catch (Exception ex) {
            log.error("Encountered error trying to send private text message: {}", ex.getMessage());
            return false;
        }
	}

    public boolean sendPrivateProductMessage(String toUserId, ProductChatMessagePayload message) {
        try {
            String jsonObj = new ObjectMapper().writer().withDefaultPrettyPrinter().writeValueAsString(message);
            jmsTemplate.send(String.format("user-%s", toUserId), messageCreator -> {
                TextMessage deliverable = messageCreator.createTextMessage();
                deliverable.setText(jsonObj);
                return deliverable;
            });

            // then save it into the database
            ChatMessage messageToSave = ChatMessage.builder()
                .messageId(generateChatMessageId())
                .typeOfMessage("product")
                .chatId(message.getChatId())
                .message(message.getMessage())
                .fromUserId(message.getFromUserId())
                .toUserId(message.getToUserId())
                .dateSent(message.getDateSent())
                .productTextDetails(message.getProductTextDetails())
            .build();
            chatMessageService.saveChat(messageToSave);

            return true;
        } catch(Exception ex) {
            log.error("Encountered error trying to send private product message: {}", ex.getMessage());
            return false;
        }
    }

    private String generateChatMessageId() {
        return UUID.randomUUID().toString()
            .replace("-", "").substring(0, 20);
    }
}
