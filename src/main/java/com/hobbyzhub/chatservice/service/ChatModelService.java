package com.hobbyzhub.chatservice.service;

import com.hobbyzhub.chatservice.repository.ChatModelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ChatModelService {
    @Autowired
    private ChatModelRepository chatModelRepository;
}
