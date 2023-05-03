package com.hobbyzhub.chatservice.controller;

import com.hobbyzhub.chatservice.service.ChatModelService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;

@Controller
@Slf4j
public class ChatModelController {
    @Autowired
    ChatModelService chatModelService;

    public ResponseEntity<?> createNewChatModel() {
        return null;
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
