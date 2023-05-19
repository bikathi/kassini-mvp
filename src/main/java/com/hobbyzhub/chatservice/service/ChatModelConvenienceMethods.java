package com.hobbyzhub.chatservice.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class ChatModelConvenienceMethods {
    @Autowired
    private ChatModelService chatModelService;

    public String checkIfChatModelExistsById(String fromUserId, String toUserId) {
        String idOptionA = String.format("%s-%s", fromUserId, toUserId);
        String idOptionB = String.format("%s-%s", toUserId, fromUserId);

        // check if one of the ids does exist
        if(chatModelService.chatModelExistsById(idOptionA)) {
            return idOptionA;
        } else if(chatModelService.chatModelExistsById(idOptionB)) {
            return idOptionB;
        }

        return null;
    }
}
