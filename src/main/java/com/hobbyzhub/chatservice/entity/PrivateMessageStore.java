package com.hobbyzhub.chatservice.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.hobbyzhub.chatservice.dto.PrivateMessageDTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder

@Document(collection = "private_chats")
public class PrivateMessageStore implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Id
	private String chatId;
	private String dateInitiated;
	
	@Builder.Default
	private Long messageCount = 0L;
	
	@Builder.Default
	private List<PrivateMessageDTO> messages = new ArrayList<>();
	
	// convenience methods
	public void addMessage(PrivateMessageDTO newMessage) {
		this.messages.add(newMessage);
	}
	
	public List<PrivateMessageDTO> getMessageList(Integer startIndex, int endIndex) {
		if(this.messages.isEmpty() != Boolean.TRUE) {
			ArrayList<PrivateMessageDTO> messagesList = 
				(ArrayList<PrivateMessageDTO>) this.messages.subList(startIndex, endIndex + 1);
			
			return messagesList; 
		} else {
			return new ArrayList<>();
		}
	}
	
	public void increaseMessageCount() {
		this.messageCount++;
	}
}
