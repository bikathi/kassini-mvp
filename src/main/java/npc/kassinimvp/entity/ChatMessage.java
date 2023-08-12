package npc.kassinimvp.entity;

import lombok.*;
import npc.kassinimvp.entity.definitions.ProductTextDetails;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
@Document(collection = "chat-messages")
public class ChatMessage {
    @Id
    private String messageId;
    private String typeOfMessage; // in this case is 'text' or 'product'
    private String chatId;
    private String message;
    private String fromUserId;
    private String toUserId;
    private String dateSent;
    private ProductTextDetails productTextDetails; // this field can be null if the text message was just a normal text
}
