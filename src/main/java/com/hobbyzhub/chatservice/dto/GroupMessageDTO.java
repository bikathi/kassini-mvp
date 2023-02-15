package com.hobbyzhub.chatservice.dto;

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
public class GroupMessageDTO {
	private String fromUserId;
	private String toGroupId;
	private String message;
	private String dateSent;
}
