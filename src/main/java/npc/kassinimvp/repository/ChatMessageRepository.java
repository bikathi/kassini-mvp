package npc.kassinimvp.repository;

import npc.kassinimvp.entity.ChatMessage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatMessageRepository extends MongoRepository<ChatMessage, String> {
    /**
     * Custom function to find chat messages by the chatId
     */
    // TODO: figure out if this query even works
    @Query("chatId: ?0")
    Page<ChatMessage> findByChatId(String chatId, Pageable pageable);
}
