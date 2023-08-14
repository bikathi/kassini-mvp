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
public class GroupItem {
    private Integer groupId;
    private String groupName;
    private String dateCreated;
    private List<AppUser> groupMembers = new ArrayList<>(10);
}
