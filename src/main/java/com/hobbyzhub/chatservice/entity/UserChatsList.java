package com.hobbyzhub.chatservice.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.hobbyzhub.chatservice.payload.request.AddDelChatRequest;

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
	private List<AddDelChatRequest> chatList = new ArrayList<>();
}
