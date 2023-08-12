package npc.kassinimvp.payload.request;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class NewChatRequest {
    private String dateCreated;
    private List<String> participantId;
}
