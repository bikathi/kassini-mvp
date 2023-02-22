package com.hobbyzhub.chatservice.payload.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AddDelChatRequest {
	private String chatId;
	private String chatType; // group or private chat
	private Boolean chatActive; // is the chat blocked(false) or active(true)
	private String chatName;
}
