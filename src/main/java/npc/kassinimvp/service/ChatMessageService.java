package npc.kassinimvp.service;

import npc.kassinimvp.entity.ChatMessage;
import npc.kassinimvp.repository.ChatMessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@Service
public class ChatMessageService {
    @Autowired
    ChatMessageRepository chatMessageRepository;

    public void saveChat(ChatMessage chatMessage) {
        chatMessageRepository.save(chatMessage);
    }

    public Page<ChatMessage> findPagedChatMessages(String chatId, Integer pageNumber, Integer pageSize) {
        Pageable page = PageRequest.of(pageNumber, pageSize);
        return chatMessageRepository.findByChatId(chatId, page);
    }
}
