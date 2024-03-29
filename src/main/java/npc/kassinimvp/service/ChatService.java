package npc.kassinimvp.service;

import npc.kassinimvp.entity.Chat;
import npc.kassinimvp.repository.ChatRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ChatService {
    @Autowired
    ChatRepository chatRepository;

    public void createNewChat(Chat newChat) {
        chatRepository.save(newChat);
    }

    public Optional<Chat> findChatByChatId(String chatId) {
        return chatRepository.findByChatId(chatId);
    }

    public void deleteChat(Chat chatToDelete) {
        chatRepository.delete(chatToDelete);
    }

    public void updateChat(Chat updatedChat) {
        chatRepository.save(updatedChat);
    }

    public Page<Chat> getPagedChatsList(String participantId, Integer pageSize, Integer pageNumber) {
        Pageable page = PageRequest.of(pageNumber, pageSize);
        return chatRepository.findAll(participantId, page);
    }
}
