package npc.kassinimvp.controller;

import npc.kassinimvp.entity.ChatModel;
import npc.kassinimvp.feign.MediaServiceFeign;
import com.hobbyzhub.chatservice.payload.request.*;
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

    @Autowired
    private MediaServiceFeign mediaServiceFeign;

    @Value("${service.api.version}")
    private String apiVersion;

    @Value("${service.organization.name}")
    private String organizationName;

    @PostMapping(value = "/group-chat/create")
    public ResponseEntity<?> newGroupChatModel(@RequestBody NewGroupChatModelRequest newGroupModelRequest) {
        String groupChatModelId = String.format(
            "%s-%s", newGroupModelRequest.getCreatedByUserId(), newGroupModelRequest.getChatName());

        // first create the proper chat model using details in the request payload
        ChatModel groupChatModel = ChatModel.builder()
            .chatId(groupChatModelId)
            .chatName(newGroupModelRequest.getChatName())
            .createdBy(newGroupModelRequest.getCreateByUserName())
            .groupChatIconLink(newGroupModelRequest.getGroupChatIconLink())
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

                // also create the location for storing group chat media
                mediaServiceFeign.createNewChatStorageLocation(groupChatModelId);

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

    @PatchMapping(value = "/group-chat/change-name")
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

    @PutMapping(value = "/group-chat/add-member")
    public ResponseEntity<?> addParticipantToChatModel(@RequestBody AddDelChatParticipantRequest addChatParticipantRequest) {
        Query findQuery = new Query().addCriteria(Criteria.where("chatId").is(addChatParticipantRequest.getChatId()));
        ChatModel.ChatParticipants newParticipant = ChatModel.ChatParticipants.builder()
            .userName(addChatParticipantRequest.getUserName())
            .isChatAdmin(addChatParticipantRequest.getIsChatAdmin())
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

    @DeleteMapping(value = "/group-chat/delete-member")
    public ResponseEntity<?> deleteParticipantFromChatModel(@RequestBody AddDelChatParticipantRequest delChatParticipantRequest) {
        Query findQuery = new Query().addCriteria(Criteria.where("chatId").is(delChatParticipantRequest.getChatId()));
        ChatModel.ChatParticipants participantToRemove = ChatModel.ChatParticipants.builder()
            .userName(delChatParticipantRequest.getUserName())
            .isChatAdmin(delChatParticipantRequest.getIsChatAdmin())
            .userId(delChatParticipantRequest.getUserId())
            .userProfilePicLink(delChatParticipantRequest.getUserProfilePicLink())
        .build();
        Update updateDefinition = new Update().pull("chatParticipants", participantToRemove);

        try {
            chatModelService.deleteParticipantFromChatModel(findQuery, updateDefinition, ChatModel.class);
            log.info("Deleted participantId: {} from groupId: {}",
                    delChatParticipantRequest.getUserId(), delChatParticipantRequest.getChatId());
            return new ResponseEntity<>(new GenericServiceResponse<>(apiVersion, organizationName,
                "Successfully added participant to chat group", HttpStatus.OK.value(), null),
            HttpStatus.OK);
        } catch(Exception ex) {
            log.error("Error deleting participant from group with groupId: {}. Caused by: {}",
                delChatParticipantRequest.getChatId(), ex.getMessage());
            return new ResponseEntity<>(new GenericServiceResponse<>(apiVersion, organizationName,
                "Error deleting participant from group", HttpStatus.INTERNAL_SERVER_ERROR.value(), null),
            HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PatchMapping("/group-chat/make-admin")
    public ResponseEntity<?> makeParticipantChatAdmin(@RequestBody MakeUserAdminRequest makeUserAdminRequest) {
        Query findQuery = new Query(
            Criteria.where("chatId").is(makeUserAdminRequest.getGroupId())
                .and("chatParticipants.userId").is(makeUserAdminRequest.getUserId())
        );
        Update updateDefinition = new Update()
                .set("chatParticipants.$.isChatAdmin", true);

        try {
            chatModelService.makeParticipantAnAdmin(findQuery, updateDefinition, ChatModel.class);
            log.info("Made participant of userId: {} into admin", makeUserAdminRequest.getUserId());
            return new ResponseEntity<>(new GenericServiceResponse<>(apiVersion, organizationName,
                "Successfully made user a chat admin", HttpStatus.OK.value(), null),
            HttpStatus.OK);
        } catch(Exception ex) {
            log.error("Error making userId: {} in groupId: {} an admin. Caused by: {}",
                makeUserAdminRequest.getUserId(), makeUserAdminRequest.getGroupId(), ex.getMessage());
            return new ResponseEntity<>(new GenericServiceResponse<>(apiVersion, organizationName,
                "Error making user an admin", HttpStatus.INTERNAL_SERVER_ERROR.value(), null),
            HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(value = "/group-chat/get-members/{chatId}")
    public ResponseEntity<?> getPagedChatParticipants(
        @PathVariable String chatId,
        @RequestParam(defaultValue = "0") String page,
        @RequestParam(defaultValue = "100", required = false) String size) {
        Integer pageNumber = Integer.valueOf(page);
        Integer pageSize = Integer.valueOf(size);

        try {
            List<ChatModel.ChatParticipants> chatParticipantsList = chatModelService.getPagedListOfParticipants(chatId, pageNumber, pageSize);

            log.info("Gotten paged list of participants for chatId: {}", chatId);
            return new ResponseEntity<>(new GenericServiceResponse<>(apiVersion, organizationName,
                "Gotten paged list of participants", HttpStatus.OK.value(), chatParticipantsList),
            HttpStatus.OK);
        } catch (Exception ex) {
            log.error("Error getting paged list of participants for chatId: {}. Caused by: {}", chatId, ex.getMessage());
            return new ResponseEntity<>(new GenericServiceResponse<>(apiVersion, organizationName,
                "Error getting list of chat participants", HttpStatus.INTERNAL_SERVER_ERROR.value(), null),
            HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping(value = "/chat-model/delete/{chatModelId}")
    public ResponseEntity<?> deleteEntireChatModel(@PathVariable String chatModelId) {
        try {
            chatModelService.deleteEntireChatModel(chatModelId);
            log.info("Delete chat model with id {}", chatModelId);

            // also delete storage location for chat model
            mediaServiceFeign.deleteChatStorageLocation(chatModelId);

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
