package npc.kassinimvp.controller;

import lombok.extern.slf4j.Slf4j;
import npc.kassinimvp.entity.Chat;
import npc.kassinimvp.entity.ChatMessage;
import npc.kassinimvp.payload.request.NewChatRequest;
import npc.kassinimvp.payload.response.GenericServiceResponse;
import npc.kassinimvp.payload.response.GetChatsResponse;
import npc.kassinimvp.payload.response.MessageResponse;
import npc.kassinimvp.service.ChatMessageService;
import npc.kassinimvp.service.ChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(value = "/api/v1/chat")
@Slf4j
public class ChatController {
    @Value("${service.api.version}")
    private String apiVersion;

    @Value("${service.organization.name}")
    private String organizationName;

    @Autowired
    private ChatService chatService;

    @Autowired
    private ChatMessageService chatMessageService;

    @PostMapping(value = "/create-chat")
    @PreAuthorize("hasAuthority('ROLE_VENDOR') or hasAuthority('ROLE_BUYER')")
    public ResponseEntity<?> createNewChat(@RequestBody NewChatRequest newChatRequest) {
        // create a Chat object based on request body
        String newChatId = generateChatId();
        Chat newChat = Chat.builder()
            .chatId(newChatId)
            .dateCreated(newChatRequest.getDateCreated())
            .participantId(newChatRequest.getParticipantId().toArray(new String[0]))
        .build();

        try {
            // save the chat object
            chatService.createNewChat(newChat);

            // return a response to the client
            log.info("New chat created of id {}", newChatId);
            return ResponseEntity.ok(new GenericServiceResponse<>(apiVersion, organizationName,
                "Successfully created new chat", HttpStatus.OK.value(), new MessageResponse(newChatId)));
        } catch(Exception ex) {
            log.error("Failed when creating new chat. Details: {}", ex.getMessage());
            return ResponseEntity.internalServerError().body(new GenericServiceResponse<>(apiVersion, organizationName,
                "Server experienced an error. Please try again", HttpStatus.INTERNAL_SERVER_ERROR.value(), new MessageResponse("Failed to create chat")));
        }
    }

    @GetMapping(value = "/get-chats")
    public ResponseEntity<?> getChats(
        @RequestParam String userId, @RequestParam(defaultValue = "0") String page, @RequestParam(defaultValue = "50", required = false) String size) {
        Integer pageNumber = Integer.parseInt(page);
        Integer pageSize = Integer.parseInt(size);
        try {
            List<GetChatsResponse> responseList = new ArrayList<>();

            // first we get the chat lists
            List<Chat> chatList = chatService.getPagedChatsList(userId, pageSize, pageNumber).getContent();
            if(chatList.isEmpty()) {
                log.info("User with ID {} has no chats", userId);
                return ResponseEntity.ok(new GenericServiceResponse<>(apiVersion, organizationName,
                        "User has no chats", HttpStatus.OK.value(), null));
            }

            // then for each chat in chatList we get the messages inside of it
            chatList.forEach(chat -> {
                List<ChatMessage> chatMessages = chatMessageService.findPagedChatMessages(chat.getChatId(), pageNumber, 100).getContent();
                if(chatMessages.isEmpty()) {
                    responseList.add(GetChatsResponse.builder()
                        .chatId(chat.getChatId())
                        .participantOneId(chat.getParticipantId()[0])
                        // TODO: this here is bound to bring issues if one of the participants deleted the chat on their side
                        .participantTwoId(chat.getParticipantId()[1])
                        .chatMessageList(chatMessages)
                    .build());
                }
            });

            log.info("Found chats for user with ID {}", userId);
            return ResponseEntity.ok(new GenericServiceResponse<>(apiVersion, organizationName,
                    "Found chats for user", HttpStatus.OK.value(), responseList));
        } catch(Exception ex) {
            log.error("Error encountered while getting chats for user {}. Details: {}", userId, ex.getMessage());
            return ResponseEntity.internalServerError().body(new GenericServiceResponse<>(apiVersion, organizationName,
                "Failed to get chats. Please Try Again.", HttpStatus.INTERNAL_SERVER_ERROR.value(), null));
        }
    }

    @DeleteMapping(value = "/delete-chat")
    @PreAuthorize("hasAuthority('ROLE_VENDOR') or hasAuthority('ROLE_BUYER')")
    public ResponseEntity<?> deleteChat(@RequestParam String participantId, @RequestParam String chatId) {
        /**
         * Deleting a chat for a user:
         * We will look into the chat with the provided chatID
         * If the userID exists in the List<String> participantIds, we remove it
         * If that List<String>participantId isEmpty() we delete the entire chat plus every message with that chatID
         * TODO: In the future, place the deletion of whole chats plus messages on JMS queue
         */
        try {
            // first find the chat in the database
            Chat targetChat = chatService.findChatByChatId(chatId).orElseThrow(() -> new RuntimeException("Chat with id: " + chatId + " not found"));

            // simulate a deletion of the user's ID from the participantID list
            List<String> participants = Arrays.asList(targetChat.getParticipantId());
            participants.remove(participantId);

            // the results of the simple simulation above decide what we do next- whether to delete the whole chat or just one participant id
            if(participants.isEmpty()) {
                // if the list is empty, then the other participant has deleted the chat. We need to fully delete it plus all messages attached to it
                // TODO: Figure out the deletion of individual messages later
                chatService.deleteChat(targetChat);
                log.info("Deleted whole chat of ID {}", chatId);
                return ResponseEntity.ok(new GenericServiceResponse<>(apiVersion, organizationName,
                    "Successfully deleted chat.", HttpStatus.OK.value(), null));
            }
            // else it means the other participant never deleted the chat on their side, so we simply update the chat to get rid of this participant's ID
            targetChat.setParticipantId(participants.toArray(new String[0]));
            chatService.updateChat(targetChat);

            log.info("Deleted participant {} from chat with chatID {}", participantId, chatId);
            return ResponseEntity.ok(new GenericServiceResponse<>(apiVersion, organizationName,
                "Successfully deleted chat.", HttpStatus.OK.value(), null));
        } catch(Exception ex) {
            log.error("Error encountered while deleting chat. Details: {}", ex.getMessage());
            return ResponseEntity.internalServerError().body(new GenericServiceResponse<>(apiVersion, organizationName,
                "Failed to delete chat. Please Try Again.", HttpStatus.INTERNAL_SERVER_ERROR.value(), null));
        }
    }

    private String generateChatId() {
        return UUID.randomUUID().toString()
            .replace("-", "").substring(0, 20);
    }
}
