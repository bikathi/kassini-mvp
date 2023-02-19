package com.hobbyzhub.chatservice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hobbyzhub.chatservice.dto.GroupMessageDTO;
import com.hobbyzhub.chatservice.dto.PrivateMessageDTO;

import jakarta.jms.TextMessage;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class StompMessageService {
	@Autowired
	JmsTemplate jmsTemplate;
	
	@Autowired
	GroupMessageStoreService groupMessageStoreSevice;
	
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
            
            convenienceMethods.addPrivateMessageToStore(message);
            return Boolean.TRUE;
        }
        catch (Exception ex) {
            log.error("Service encountered error trying to send private message: {}", ex.getMessage());
            return Boolean.FALSE;
        }
	}
	
	public boolean sendGroupMessage(String toGroupId, GroupMessageDTO message) {
		try {
            String jsonObj = new ObjectMapper().writer().withDefaultPrettyPrinter().writeValueAsString(message);
            jmsTemplate.send("group-" + toGroupId, messageCreator -> {
                TextMessage deliverable = messageCreator.createTextMessage();
                deliverable.setText(jsonObj);
                return deliverable;
            });
            
            convenienceMethods.addGroupMessageToStore(message);
            return Boolean.TRUE;
        }
        catch (Exception ex) {
        	log.error("Service encountered error trying to send group message: {}", ex.getMessage());
            return Boolean.FALSE;
        }
	}
}
