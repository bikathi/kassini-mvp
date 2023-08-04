package npc.kassinimvp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import npc.kassinimvp.dto.PrivateMessageDTO;

import jakarta.jms.TextMessage;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class StompMessageService {
	@Autowired
	JmsTemplate jmsTemplate;
	
	@Autowired
	MessageStoreConvenienceMethods convenienceMethods;
	
	public Boolean sendPrivateMessage(String toUserId, PrivateMessageDTO message) {
		try {
            String jsonObj = new ObjectMapper().writer().withDefaultPrettyPrinter().writeValueAsString(message);
            // first send the message to the queue
            jmsTemplate.send("user-" + toUserId, messageCreator -> {
                TextMessage deliverable = messageCreator.createTextMessage();
                deliverable.setText(jsonObj);
                return deliverable;
            });

            // then store it once we are sure it is on the queue
            convenienceMethods.storePrivateMessage(message);
            return Boolean.TRUE;
        } catch (Exception ex) {
            log.error("Encountered error trying to send private message: {}", ex.getMessage());
            return Boolean.FALSE;
        }
	}
}
