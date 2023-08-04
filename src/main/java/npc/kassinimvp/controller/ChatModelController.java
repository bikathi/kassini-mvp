package npc.kassinimvp.controller;

import npc.kassinimvp.entity.ChatModel;
import npc.kassinimvp.payload.request.*;
import npc.kassinimvp.payload.response.GenericServiceResponse;
import npc.kassinimvp.service.ChatModelService;
import npc.kassinimvp.service.DestinationManagementService;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/api")
@Setter
@Slf4j
public class ChatModelController {
    @Autowired
    ChatModelService chatModelService;

    @Autowired
    DestinationManagementService destinationService;

    @Value("${service.api.version}")
    private String apiVersion;

    @Value("${service.organization.name}")
    private String organizationName;

    @PostMapping(value = "/private-chat/create")
    public ResponseEntity<?> newPrivateChatModel(@RequestBody NewPrivateChatModelRequest newChatModelRequest) {
        String privateChatModelId = String.format(
            "%s-%s", newChatModelRequest.getFromUserId(), newChatModelRequest.getToUserId());

        // first create the proper chat model using details in the request payload
        ChatModel privateChatModel = ChatModel.builder()
            .chatId(privateChatModelId)
            .chatName(newChatModelRequest.getFromUserId() + newChatModelRequest.getToUserId())
            .createdBy(newChatModelRequest.getFromUserId())
            .isGroupChat(false)
        .build();

        // add the creator of the chat as the first participant
        privateChatModel.addChatParticipant(
            ChatModel.ChatParticipants.builder()
                .userId(newChatModelRequest.getFromUserId())
                .userName(newChatModelRequest.getFromUserName())
                .userProfilePicLink(newChatModelRequest.getFromUserProfilePicLink())
            .build()
        );

        // add the recipient of the first message as the second chat participant
        privateChatModel.addChatParticipant(
            ChatModel.ChatParticipants.builder()
                .userId(newChatModelRequest.getToUserId())
                .userName(newChatModelRequest.getToUserName())
                .userProfilePicLink(newChatModelRequest.getToUserProfilePicLink())
            .build()
        );

        try {
            // try saving the new ChatModel details
            chatModelService.createNewChatModel(privateChatModel);

            // create a new destination with these
            boolean destinationCreated = destinationService.createPrivateDestination(privateChatModelId);
            if(!destinationCreated) {
                log.warn("Created private chat but failed to create message destination");
                return new ResponseEntity<>(new GenericServiceResponse<>(apiVersion, organizationName,
                    "Failed to create chat message destination", HttpStatus.PARTIAL_CONTENT.value(), null),
                HttpStatus.PARTIAL_CONTENT);
            }
            log.info("Created PrivateChat of id: {} and message destination", privateChatModelId);
            return new ResponseEntity<>(new GenericServiceResponse<>(apiVersion, organizationName,
                "Successfully created new private chat model", HttpStatus.OK.value(), null),
            HttpStatus.OK);
        } catch(Exception ex) {
            log.error("Error creating private chat. Details: {}", ex.getMessage());
            return new ResponseEntity<>(new GenericServiceResponse<>(apiVersion, organizationName,
                "Error creating group chat", HttpStatus.INTERNAL_SERVER_ERROR.value(), null),
            HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping(value = "/chat-model/delete/{chatModelId}")
    public ResponseEntity<?> deleteEntireChatModel(@PathVariable String chatModelId) {
        try {
            chatModelService.deleteEntireChatModel(chatModelId);
            log.info("Delete chat model with id {}", chatModelId);

            return new ResponseEntity<>(new GenericServiceResponse<>(apiVersion, organizationName,
                "Successfully deleted chat model", HttpStatus.OK.value(), null),
            HttpStatus.OK);
        } catch(Exception ex) {
            log.error("Error deleting group with groupId: {}. Caused by: {}", chatModelId, ex.getMessage());
            return new ResponseEntity<>(new GenericServiceResponse<>(apiVersion, organizationName,
                "Error deleting group", HttpStatus.INTERNAL_SERVER_ERROR.value(), null),
            HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
