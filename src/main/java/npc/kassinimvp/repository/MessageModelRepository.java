package npc.kassinimvp.repository;

import npc.kassinimvp.entity.MessageModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MessageModelRepository extends MongoRepository<MessageModel, String> {
    /**
     * A custom method to help find the list of messages for a particular chat by the chatModelId
     */
    Page<MessageModel> findAllByChatModelId(String chatId, Pageable page);
}
