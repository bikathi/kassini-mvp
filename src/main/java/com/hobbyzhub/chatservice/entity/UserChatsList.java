package com.hobbyzhub.chatservice.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "user_chat_list")
public class UserChatsList implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Id
	private String userId; // same as real user's id
	
	@Builder.Default
	private List<ChatInfo> chatList = new ArrayList<>();
	
	@Builder
	@Getter
	private static class ChatInfo {
		private String chatId;
		private String chatType; // group or private chat
		private Boolean chatActive; // is the chat blocked(false) or active(true)
		private String chatName;
	}
}
