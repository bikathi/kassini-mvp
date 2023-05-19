package com.hobbyzhub.chatservice.service;

import com.hobbyzhub.chatservice.dto.GroupMessageDTO;
import com.hobbyzhub.chatservice.dto.PrivateMessageDTO;
import com.hobbyzhub.chatservice.entity.MessageModel;
import com.hobbyzhub.chatservice.repository.MessageModelRepository;
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

    public void storeGroupChatMessage(GroupMessageDTO groupMessageDTO) {
        // first convert the DTO to a MongoDB-Compatible message
        // the id of the message is manually generated
        MessageModel message = MessageModel.builder()
            .chatModelId(groupMessageDTO.getToGroupId())
            .messageModelId(
                modelsConvenienceMethods.createMessageModelId(groupMessageDTO.getFromUserId())
            )
            .messageString(groupMessageDTO.getMessage())
            .fromUserId(groupMessageDTO.getFromUserId())
            .toDestinationId(groupMessageDTO.getToGroupId())
            .dateTimeSent(groupMessageDTO.getDateSent())
        .build();

        // then store the messages in the MessageStore
        messageModelRepository.save(message);
        log.info(
            "Stored private from UserId {} to GroupId {}",
            groupMessageDTO.getFromUserId(),
            groupMessageDTO.getToGroupId()
        );
    }
}
