package com.hobbyzhub.chatservice.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.hobbyzhub.chatservice.dto.GroupMessageDTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder

@Document(collection = "group_chats")
public class GroupMessageStore implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Id
	private String groupId;
	private String dateInitiated;
	private Long messageCount;
	
	@Builder.Default
	private List<GroupMessageDTO> messages = new LinkedList<>();
	
	// convenience methods
	public boolean addMessage(GroupMessageDTO newMessage) {
		return this.messages.add(newMessage);
	}
	
	public List<GroupMessageDTO> getMessageList(Integer startIndex, int endIndex) {
		if(this.messages.isEmpty() != Boolean.TRUE) {
			ArrayList<GroupMessageDTO> messagesList = 
				(ArrayList<GroupMessageDTO>) this.messages.subList(startIndex, endIndex + 1);
			
			return messagesList; 
		} else {
			return new ArrayList<>();
		}
	}
	
}
