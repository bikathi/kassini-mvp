package npc.kassinimvp.repository;

import npc.kassinimvp.entity.Groups;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GroupsRepository extends MongoRepository<Groups, String> {
}
