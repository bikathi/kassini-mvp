package com.hobbyzhub.chatservice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hobbyzhub.chatservice.repository.PrivateMessageStoreRepository;

@Service
public class PrivateMessageStoreService {
	@Autowired
	PrivateMessageStoreRepository storeRepository;
}
