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
    private List<String> participantIds;
    private List<ChatMessage> chatMessageList;
}
