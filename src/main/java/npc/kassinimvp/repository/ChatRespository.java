package npc.kassinimvp.repository;

import npc.kassinimvp.entity.Chat;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatRespository extends MongoRepository<Chat, String> {
}
