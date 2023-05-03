package com.hobbyzhub.chatservice.controller;

import com.hobbyzhub.chatservice.service.MessageModelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api")
public class MessageModelController {
    @Autowired
    MessageModelService messageModelService;

    public ResponseEntity<?> getPagedMessageList() {
        return null;
    }
}
