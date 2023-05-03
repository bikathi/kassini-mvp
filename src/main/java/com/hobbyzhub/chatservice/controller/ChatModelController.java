package com.hobbyzhub.chatservice.controller;

import com.hobbyzhub.chatservice.entity.ChatModel;
import com.hobbyzhub.chatservice.payload.request.NewGroupChatModelRequest;
import com.hobbyzhub.chatservice.payload.response.GenericServiceResponse;
import com.hobbyzhub.chatservice.service.ChatModelService;
import com.hobbyzhub.chatservice.service.DestinationManagementService;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping(value = "/group-chat/create")
    public ResponseEntity<?> newGroupChatModel(@RequestBody NewGroupChatModelRequest newGroupModelRequest) {
        // TODO: REMEMBER TO FIX THIS GROUP CHAT ID PART
        String groupChatModelId = "";

        // first create the proper chat model using details in the request payload
        ChatModel groupChatModel = ChatModel.builder()
            .chatId(groupChatModelId)
            .chatName(newGroupModelRequest.getChatName())
            .createdBy(newGroupModelRequest.getCreateByUserName())
            .isGroupChat(true)
        .build();

        // then add the creator of the group as the first participant
        groupChatModel.addChatParticipant(
            ChatModel.ChatParticipants.builder()
                .userId(newGroupModelRequest.getCreatedByUserId())
                .userName(newGroupModelRequest.getCreateByUserName())
                .userProfilePicLink(newGroupModelRequest.getUserProfilePicLink())
                .isChatAdmin(true)
            .build()
        );

        try {
            // try saving the new ChatModel details
            chatModelService.createNewChatModel(groupChatModel);

            // create a new destination with these
            boolean destinationCreated = destinationService.createGroupDestination(groupChatModelId);
            if(!destinationCreated) {
                log.error("Created group but failed to create message destination");
                return new ResponseEntity<>(new GenericServiceResponse<>(apiVersion, organizationName,
                    "Failed to create group messages destination", HttpStatus.PARTIAL_CONTENT.value(), null),
                HttpStatus.INTERNAL_SERVER_ERROR);
            }
            log.error("Created GroupChat of id: {} and message destination", groupChatModelId);
            return new ResponseEntity<>(new GenericServiceResponse<>(apiVersion, organizationName,
                "Successfully created new GroupChat model", HttpStatus.OK.value(), null),
            HttpStatus.INTERNAL_SERVER_ERROR);
        }catch(Exception ex) {
            log.error("Error creating group chat details: {}", ex.getMessage());
            return new ResponseEntity<>(new GenericServiceResponse<>(apiVersion, organizationName,
                "Error creating group chat details", HttpStatus.INTERNAL_SERVER_ERROR.value(), null),
            HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping(value = "/private-chat/create")
    // TODO: COME COMPLETE THIS LATER
    public ResponseEntity<?> newPrivateChatModel(@RequestBody NewGroupChatModelRequest newGroupModelRequest) {
        // TODO: REMEMBER TO FIX THIS GROUP CHAT ID PART
        String groupChatModelId = "";

        // first create the proper chat model using details in the request payload
        ChatModel groupChatModel = ChatModel.builder()
                .chatId(groupChatModelId)
                .chatName(newGroupModelRequest.getChatName())
                .createdBy(newGroupModelRequest.getCreateByUserName())
                .isGroupChat(true)
                .build();

        // then add the creator of the group as the first participant
        groupChatModel.addChatParticipant(
                ChatModel.ChatParticipants.builder()
                        .userId(newGroupModelRequest.getCreatedByUserId())
                        .userName(newGroupModelRequest.getCreateByUserName())
                        .userProfilePicLink(newGroupModelRequest.getUserProfilePicLink())
                        .isChatAdmin(true)
                        .build()
        );

        try {
            // try saving the new ChatModel details
            chatModelService.createNewChatModel(groupChatModel);

            // create a new destination with these
            boolean destinationCreated = destinationService.createGroupDestination(groupChatModelId);
            if(!destinationCreated) {
                log.error("Created group but failed to create message destination");
                return new ResponseEntity<>(new GenericServiceResponse<>(apiVersion, organizationName,
                        "Failed to create group messages destination", HttpStatus.PARTIAL_CONTENT.value(), null),
                        HttpStatus.INTERNAL_SERVER_ERROR);
            }
            log.error("Created GroupChat of id: {} and message destination", groupChatModelId);
            return new ResponseEntity<>(new GenericServiceResponse<>(apiVersion, organizationName,
                    "Successfully created new GroupChat model", HttpStatus.OK.value(), null),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }catch(Exception ex) {
            log.error("Error creating group chat details: {}", ex.getMessage());
            return new ResponseEntity<>(new GenericServiceResponse<>(apiVersion, organizationName,
                    "Error creating group chat details", HttpStatus.INTERNAL_SERVER_ERROR.value(), null),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<?> updateChatModelName() {
        return null;
    }

    public ResponseEntity<?> addParticipantToChatModel() {
        return null;
    }

    public ResponseEntity<?> deleteParticipantFromChatModel() {
        return null;
    }
}
