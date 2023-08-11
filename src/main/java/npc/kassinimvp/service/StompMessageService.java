package npc.kassinimvp.service;

import npc.kassinimvp.payload.request.ProductChatMessagePayload;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;
import npc.kassinimvp.payload.request.TextChatMessagePayload;

import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.jms.TextMessage;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class StompMessageService {
	@Autowired
	JmsTemplate jmsTemplate;
	
	public boolean sendPrivateTextMessage(String toUserId, TextChatMessagePayload message) {
		try {
            String jsonObj = new ObjectMapper().writer().withDefaultPrettyPrinter().writeValueAsString(message);
            // send the message to the queue
            jmsTemplate.send(String.format("user-%s", toUserId), messageCreator -> {
                TextMessage deliverable = messageCreator.createTextMessage();
                deliverable.setText(jsonObj);
                return deliverable;
            });

            // TODO: then save it into the database
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

            // TODO: then save it into the database
            return true;
        } catch(Exception ex) {
            log.error("Encountered error trying to send private product message: {}", ex.getMessage());
            return false;
        }
    }
}
