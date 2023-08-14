package npc.kassinimvp.service;

import npc.kassinimvp.entity.Groups;
import npc.kassinimvp.entity.definitions.GroupItem;
import npc.kassinimvp.repository.GroupsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

@Service
public class GroupsService {
    @Autowired
    GroupsRepository groupsRepository;

    @Autowired
    MongoTemplate mongoTemplate;

    public void createGroups(Groups newGroups) {
        groupsRepository.save(newGroups);
    }

    public void addNewGroupItem(String vendorId, GroupItem newGroupItem) {
        Query findQuery = new Query(Criteria.where("vendorId").is(vendorId));
        Update updateDefinition = new Update().push("groupItems", newGroupItem);

        FindAndModifyOptions findAndModifyOptions = new FindAndModifyOptions().returnNew(false).upsert(false);
        mongoTemplate.findAndModify(findQuery, updateDefinition, findAndModifyOptions, Groups.class, "vendor-groups");
    }
}
