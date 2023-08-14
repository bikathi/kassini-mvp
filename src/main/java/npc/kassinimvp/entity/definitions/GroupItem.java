package npc.kassinimvp.entity.definitions;

import lombok.*;
import npc.kassinimvp.entity.AppUser;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GroupItem {
    private String groupId;
    private String groupName;
    private List<AppUser> groupMembers = new ArrayList<>(10);
}
