package npc.kassinimvp.controller;

import lombok.extern.slf4j.Slf4j;
import npc.kassinimvp.entity.Chat;
import npc.kassinimvp.payload.request.NewChatRequest;
import npc.kassinimvp.payload.response.GenericServiceResponse;
import npc.kassinimvp.payload.response.MessageResponse;
import npc.kassinimvp.service.ChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

    public ResponseEntity<?> getChats() {
        return null;
    }

    public ResponseEntity<?> deleteChat() {
        return null;
    }

    private String generateChatId() {
        return UUID.randomUUID().toString()
            .replace("-", "").substring(0, 20);
    }
}
