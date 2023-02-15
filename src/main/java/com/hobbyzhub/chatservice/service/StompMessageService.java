package com.hobbyzhub.chatservice.service;

import org.springframework.stereotype.Service;

import com.hobbyzhub.chatservice.dto.GroupMessageDTO;
import com.hobbyzhub.chatservice.dto.PrivateMessageDTO;

@Service
public class StompMessageService {
	public boolean sendPrivateMessage(String toUserId, PrivateMessageDTO message) {
		return Boolean.FALSE;
	}
	
	public boolean sendGroupMessage(String toGroupId, GroupMessageDTO message) {
		return Boolean.FALSE;
	}
}
