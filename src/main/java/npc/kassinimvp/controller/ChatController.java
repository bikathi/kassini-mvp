package npc.kassinimvp.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/v1/chat")
@Slf4j
public class ChatController {
    @PostMapping(value = "/create-chat")
    public ResponseEntity<?> createNewChat() {
        return null;
    }

    public ResponseEntity<?> getChats() {
        return null;
    }

    public ResponseEntity<?> deleteChat() {
        return null;
    }
}
