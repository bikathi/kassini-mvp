package npc.kassinimvp.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@AllArgsConstructor
@ToString
@NoArgsConstructor
@Builder
@Document(collection = "user-chats")
public class Chat {
    @Id
    private String chatId;
    private String dateCreated;
    private String[] participantId = new String[2]; // the max capacity for a private chat is 2
}
