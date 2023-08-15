package npc.kassinimvp.entity;

import lombok.*;
import npc.kassinimvp.entity.definitions.GroupItem;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
@Document(collection = "vendor-groups")
public class Groups {
    @Id
    private String groupId;
    private String vendorId;
    private List<GroupItem> groupItems;
}
