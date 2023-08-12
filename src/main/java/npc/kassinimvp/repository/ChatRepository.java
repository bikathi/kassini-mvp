package npc.kassinimvp.repository;

import npc.kassinimvp.entity.Chat;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ChatRepository extends MongoRepository<Chat, String> {
    /**
     * Custom method to return chatByID
     */
    Optional<Chat> findByChatId(String chatId);

}
