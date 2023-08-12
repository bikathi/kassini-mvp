package npc.kassinimvp.payload.response;

import lombok.*;
import npc.kassinimvp.entity.ChatMessage;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@ToString
@NoArgsConstructor
@Builder
public class GetChatsResponse {
    private String chatId;
    private String participantOneId;
    private String participantTwoId;
    private List<ChatMessage> chatMessageList;
}
