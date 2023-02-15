package com.hobbyzhub.chatservice.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class PrivateMessageDTO {
	private String fromUserId;
	private String toUserId;
	private String message;
	private String dateSent;
}
