package npc.kassinimvp.service;

import npc.kassinimvp.entity.ChatMessage;
import npc.kassinimvp.repository.ChatMessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ChatMessageService {
    @Autowired
    ChatMessageRepository chatMessageRepository;

    public void saveChat(ChatMessage chatMessage) {
        chatMessageRepository.save(chatMessage);
    }
}
