package com.hobbyzhub.chatservice.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.util.List;

@Document(collection = "chat-model-collection")
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ChatModel implements Serializable {
    @Id
    private String chatId;
    private String chatName;
    private String isGroupChat;

    private List<String> users;

    private String latestMessage;
}
