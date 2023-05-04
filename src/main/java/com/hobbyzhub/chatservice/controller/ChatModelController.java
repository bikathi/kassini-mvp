package com.hobbyzhub.chatservice.controller;

import com.hobbyzhub.chatservice.entity.ChatModel;
import com.hobbyzhub.chatservice.payload.request.AddChatParticipantRequest;
import com.hobbyzhub.chatservice.payload.request.NewGroupChatModelRequest;
import com.hobbyzhub.chatservice.payload.request.NewPrivateChatModelRequest;
import com.hobbyzhub.chatservice.payload.request.ChangeGroupChatModelNameRequest;
import com.hobbyzhub.chatservice.payload.response.GenericServiceResponse;
import com.hobbyzhub.chatservice.service.ChatModelService;
import com.hobbyzhub.chatservice.service.DestinationManagementService;
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
                log.warn("Created group but failed to create message destination");
                return new ResponseEntity<>(new GenericServiceResponse<>(apiVersion, organizationName,
                    "Failed to create group messages destination", HttpStatus.PARTIAL_CONTENT.value(), null),
                HttpStatus.PARTIAL_CONTENT);
            }
            log.info("Created GroupChat of id: {} and message destination", groupChatModelId);
            return new ResponseEntity<>(new GenericServiceResponse<>(apiVersion, organizationName,
                "Successfully created new GroupChat model", HttpStatus.OK.value(), null),
            HttpStatus.OK);
        } catch(Exception ex) {
            log.error("Error creating group chat. Details: {}", ex.getMessage());
            return new ResponseEntity<>(new GenericServiceResponse<>(apiVersion, organizationName,
                "Error creating group chat", HttpStatus.INTERNAL_SERVER_ERROR.value(), null),
            HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping(value = "/private-chat/create")
    // TODO: COME COMPLETE THIS LATER
    public ResponseEntity<?> newPrivateChatModel(@RequestBody NewPrivateChatModelRequest newChatModelRequest) {
        // TODO: REMEMBER TO FIX THIS PRIVATE CHAT ID PART
        String privateChatModelId = "";

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

    @PostMapping(value = "/group-chat/change-name")
    public ResponseEntity<?> updateGroupChatModelName(@RequestBody ChangeGroupChatModelNameRequest updateNameRequest) {
        Query findQuery = new Query().addCriteria(Criteria.where("chatId").is(updateNameRequest.getChatId()));
        Update updateDefinition = new Update().set("chatName", updateNameRequest.getNewName());
        FindAndModifyOptions findAndModifyOptions = new FindAndModifyOptions().returnNew(false).upsert(false);

        try {
            chatModelService.updateChatModelName(findQuery, updateDefinition, findAndModifyOptions, ChatModel.class);
            log.info("Updated group chat name for Id: {}", updateNameRequest.getChatId());
            return new ResponseEntity<>(new GenericServiceResponse<>(apiVersion, organizationName,
                "Successfully updated chat name", HttpStatus.OK.value(), null),
            HttpStatus.OK);
        } catch(Exception ex) {
            log.error("Error updating name for groupId: {}. Caused by: {}", updateNameRequest.getChatId(), ex.getMessage());
            return new ResponseEntity<>(new GenericServiceResponse<>(apiVersion, organizationName,
                "Error updating group name", HttpStatus.INTERNAL_SERVER_ERROR.value(), null),
            HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<?> addParticipantToChatModel(@RequestBody AddChatParticipantRequest addChatParticipantRequest) {
        // find the document we want to update
        Query findQuery = new Query().addCriteria(Criteria.where("chatId").is(addChatParticipantRequest.getChatId()));
        ChatModel.ChatParticipants newParticipant = ChatModel.ChatParticipants.builder()
            .userName(addChatParticipantRequest.getUserName())
            .isChatAdmin(false)
            .userId(addChatParticipantRequest.getUserId())
            .userProfilePicLink(addChatParticipantRequest.getUserProfilePicLink())
        .build();
        Update updateDefinition = new Update().push("chatParticipants", newParticipant);
        FindAndModifyOptions findAndModifyOptions = new FindAndModifyOptions().returnNew(false).upsert(false);

        try {
            chatModelService.addParticipantToChatModel(findQuery, updateDefinition, findAndModifyOptions, ChatModel.class);
            log.info("Added new participant to groupId: {}", addChatParticipantRequest.getChatId());
            return new ResponseEntity<>(new GenericServiceResponse<>(apiVersion, organizationName,
                "Successfully added participant to chat group", HttpStatus.OK.value(), null),
            HttpStatus.OK);
        } catch (Exception ex) {
            log.error("Error adding new participant to groupId: {}. Caused by: {}", addChatParticipantRequest.getChatId(), ex.getMessage());
            return new ResponseEntity<>(new GenericServiceResponse<>(apiVersion, organizationName,
                "Error adding new participant to group chat", HttpStatus.INTERNAL_SERVER_ERROR.value(), null),
            HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<?> deleteParticipantFromChatModel() {
        return null;
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
