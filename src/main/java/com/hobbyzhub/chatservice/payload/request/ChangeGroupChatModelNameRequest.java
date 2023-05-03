package com.hobbyzhub.chatservice.payload.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ChangeGroupChatModelNameRequest {
    private String chatId;
    private String newName;
}
