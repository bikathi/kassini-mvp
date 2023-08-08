package npc.kassinimvp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.jms.TextMessage;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class StompMessageService {
	@Autowired
	JmsTemplate jmsTemplate;
	
	public boolean sendPrivateMessage(String toUserId, ChatMessagePayload message) {
		try {
            String jsonObj = new ObjectMapper().writer().withDefaultPrettyPrinter().writeValueAsString(message);
            // first send the message to the queue
            jmsTemplate.send(String.format("user-%s", toUserId), messageCreator -> {
                TextMessage deliverable = messageCreator.createTextMessage();
                deliverable.setText(jsonObj);
                return deliverable;
            });
            return true;
        } catch (Exception ex) {
            log.error("Encountered error trying to send private message: {}", ex.getMessage());
            return false;
        }
	}
}
