package npc.kassinimvp.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Document(collection = "chat-list")
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

    // this field will be empty for private chats
    private String createdBy;

    // group chat icon
    private String groupChatIconLink;

    private Boolean isGroupChat;
    private List<ChatParticipants> chatParticipants = new ArrayList<>();

    public void addChatParticipant(ChatParticipants chatParticipant) {
        this.chatParticipants.add(chatParticipant);
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @Builder
    public static class ChatParticipants {
        private String userId;
        private String userName;

        // this is only for group chats. private chats have no admins
        // so in group chats, this will be false
        private Boolean isChatAdmin;
        private String userProfilePicLink;
    }
}
