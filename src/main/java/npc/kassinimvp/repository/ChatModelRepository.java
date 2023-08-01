package npc.kassinimvp.repository;

import npc.kassinimvp.entity.ChatModel;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatModelRepository extends MongoRepository<ChatModel, String> {
}
