package npc.kassinimvp.payload.request;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class TextChatMessagePayload {
    private String typeOfMessage; // in this case is 'text'
    private String message;
    private String fromUserId;
    private String toUserId;
    private String dateSent;
}