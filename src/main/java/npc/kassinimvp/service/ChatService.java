package npc.kassinimvp.service;

import npc.kassinimvp.entity.Chat;
import npc.kassinimvp.repository.ChatRespository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ChatService {
    @Autowired
    ChatRespository chatRespository;

    public void createNewChat(Chat newChat) {
        chatRespository.save(newChat);
    }
}
