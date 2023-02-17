package com.hobbyzhub.chatservice.entity;

import java.io.Serializable;
import java.util.Queue;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.hobbyzhub.chatservice.dto.PrivateMessageDTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "private_chats")
public class PrivateMessageStore implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Id
	private String messagesId;
	private String dateInitiated;
	private Integer messageCount;
	
	private Queue<PrivateMessageDTO> messages;
}
