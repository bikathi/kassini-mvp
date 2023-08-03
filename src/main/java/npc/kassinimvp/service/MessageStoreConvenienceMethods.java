package npc.kassinimvp.service;

import npc.kassinimvp.dto.PrivateMessageDTO;
import npc.kassinimvp.entity.MessageModel;
import npc.kassinimvp.repository.MessageModelRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class MessageStoreConvenienceMethods {
    @Autowired
    private MessageModelRepository messageModelRepository;

    @Autowired
    private ModelsConvenienceMethods modelsConvenienceMethods;

    public void storePrivateMessage(PrivateMessageDTO privateMessageDTO) {
        // first convert the DTO to a MongoDB-Compatible message
        // the id of the message is manually generated
        MessageModel message = MessageModel.builder()
            .chatModelId(modelsConvenienceMethods.checkIfChatModelExistsById(
                privateMessageDTO.getFromUserId(), privateMessageDTO.getToUserId()
            ))
            .messageModelId(
                modelsConvenienceMethods.createMessageModelId(privateMessageDTO.getFromUserId()
            ))
            .messageString(privateMessageDTO.getMessage())
            .fromUserId(privateMessageDTO.getFromUserId())
            .toDestinationId(privateMessageDTO.getToUserId())
            .dateTimeSent(privateMessageDTO.getDateSent())
        .build();

        // then store the message in the MessageStore
        messageModelRepository.save(message);
        log.info(
            "Stored private from UserId {} to UserId {}",
            privateMessageDTO.getFromUserId(),
            privateMessageDTO.getToUserId()
        );
    }
}
