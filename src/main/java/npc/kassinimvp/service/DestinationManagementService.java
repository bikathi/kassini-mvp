package npc.kassinimvp.service;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import npc.kassinimvp.dto.PrivateMessageDTO;

import jakarta.jms.TextMessage;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class DestinationManagementService {
	@Autowired
	JmsTemplate jmsTemplate;
	
	@Autowired
	MessageStoreConvenienceMethods convenienceMethods;
	
	public boolean createPrivateDestination(String userId) {
		PrivateMessageDTO internalPrivateMessage = 
			PrivateMessageDTO.builder()
			.fromUserId("Hobbyzhub")
			.toUserId(userId)
			.message("Welcome to Hobbyzhub. We're thrilled to have you.")
			.dateSent(LocalDate.now().toString())
			.build();
		
		try {
			String internalMessage = new ObjectMapper().writer().withDefaultPrettyPrinter()
				.writeValueAsString(internalPrivateMessage);
			
			jmsTemplate.send("user-" + userId, messageCreator -> {
                TextMessage deliverable = messageCreator.createTextMessage();
                deliverable.setText(internalMessage);
                return deliverable;
            });
			
			convenienceMethods.storePrivateMessage(internalPrivateMessage);
			log.info("JMSTemplate created new private destination of ID: {}", "user-" + userId);
			return true;
		} catch(Exception ex) {
			log.error("JMSTemplate error creating private destination: {}", ex.getMessage());
			return false;
		}
	}
}