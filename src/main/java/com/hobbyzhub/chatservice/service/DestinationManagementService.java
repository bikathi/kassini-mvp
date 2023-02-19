package com.hobbyzhub.chatservice.service;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.jms.TextMessage;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class DestinationManagementService {
	@Autowired
	JmsTemplate jmsTemplate;
	
	public boolean createPrivateDestination(String userId) {
		try {
			String internalMessage = new ObjectMapper().writer().withDefaultPrettyPrinter()
				.writeValueAsString(
					InternalMessageSource.builder()
					.id("HobbyzHub")
					.dateSent(LocalDate.now().toString())
					.message("Welcome to HobbyzHub. We're glad to have you.")
					.build());
			
			jmsTemplate.send("user-" + userId, messageCreator -> {
                TextMessage deliverable = messageCreator.createTextMessage();
                deliverable.setText(internalMessage);
                return deliverable;
            });
			
			log.info("JMSTemplate created new private destination of ID: {}", "user-" + userId);
			return Boolean.TRUE;
		} catch(Exception ex) {
			log.error("JMSTemplate error creating private destination: {}", ex.getMessage());
			return Boolean.FALSE;
		}
	}
	
	public boolean createGroupDestination(String groupId) {
		try {
			String dateToday = LocalDate.now().toString();
			String internalMessage = new ObjectMapper().writer().withDefaultPrettyPrinter()
				.writeValueAsString(
					InternalMessageSource.builder()
					.id("HobbyzHub")
					.dateSent(dateToday)
					.message("Group Created On: " + dateToday)
					.build());
			
			jmsTemplate.send("group-" + groupId, messageCreator -> {
	            TextMessage deliverable = messageCreator.createTextMessage();
	            deliverable.setText(internalMessage);
	            return deliverable;
	        });
			
			log.info("JMSTemplate created new group destination of ID: {}", "group-" + groupId);
			return Boolean.TRUE;
		} catch(Exception ex) {
			log.error("JMSTemplate error creating group destination: {}", ex.getMessage());
			return Boolean.FALSE;
		}
	}
	
	@Getter
	@Setter
	@NoArgsConstructor
	@AllArgsConstructor
	@ToString
	@Builder
	static class InternalMessageSource {
		private String id;
		private String message;
		private String dateSent;
	}
}
