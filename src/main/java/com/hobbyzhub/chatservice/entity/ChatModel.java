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
@Builder
public class ChatModel implements Serializable {
    @Id
    private String chatId;
    private String chatName;

    private String isGroupChat;

    private List<ChatParticipants> chatParticipants;

    private String latestMessage;
}


@Getter
@Setter
@AllArgsConstructor
class ChatParticipants {
    private String userId;
    private String userName;

    // this is only for group chats. private chats have no admins
    private Boolean isChatAdmin;
}
