package npc.kassinimvp.entity;

import lombok.*;
import npc.kassinimvp.entity.definitions.ProductTextDetails;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ChatMessage {
    private String typeOfMessage; // in this case is 'text' or 'product'
    private String message;
    private String fromUserId;
    private String toUserId;
    private String dateSent;
    private ProductTextDetails productTextDetails; // this field can be null if the text message was just a normal text
}
