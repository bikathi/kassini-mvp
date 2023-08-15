package npc.kassinimvp.payload.request;

import lombok.*;
import java.util.Set;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class CreateGroupItemRequest {
    private String groupName;
    private Set<MinimAppUser> groupMembers;

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @ToString
    public static class MinimAppUser {
        private String userId;
        private String bioName;
        private String firstName;
        private String lastName;
        private Long phoneNumber;
    }
}
