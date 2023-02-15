package com.hobbyzhub.chatservice.service;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

import jakarta.jms.TextMessage;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class DestinationManagementService {
	@Autowired
	JmsTemplate jmsTemplate;
	
	public boolean createPrivateDestination(String userId) {
		try {
			jmsTemplate.send("user-" + userId, messageCreator -> {
                TextMessage deliverable = messageCreator.createTextMessage();
                deliverable.setText("Testing user's destination availability");
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
			jmsTemplate.send("group-" + groupId, messageCreator -> {
	            TextMessage deliverable = messageCreator.createTextMessage();
	            
	            String dateToday = LocalDate.now().toString();
	            deliverable.setText(String.format("Group created on: {}", dateToday));
	            return deliverable;
	        });
			
			log.info("JMSTemplate created new group destination of ID: {}", "group-" + groupId);
			return Boolean.TRUE;
		} catch(Exception ex) {
			log.error("JMSTemplate error creating group destination: {}", ex.getMessage());
			return Boolean.FALSE;
		}
	}
}
