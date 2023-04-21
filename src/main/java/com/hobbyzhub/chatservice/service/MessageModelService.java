package com.hobbyzhub.chatservice.service;

import com.hobbyzhub.chatservice.repository.MessageModelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MessageModelService {
    @Autowired
    private MessageModelRepository messageModelRepository;
}
