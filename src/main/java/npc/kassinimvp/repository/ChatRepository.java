package npc.kassinimvp.repository;

import npc.kassinimvp.entity.Chat;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ChatRepository extends MongoRepository<Chat, String> {
    /**
     * Custom method to return chatByID
     */
    Optional<Chat> findByChatId(String chatId);

    /**
     * Custom method to find paged list of chats
     */
    @Query("{ 'participantId': { $in: [?0] } }")
    // TODO: Test this query to ensure it works and makes sense
    Page<Chat> findAll(String participantId, Pageable pageable);
}
