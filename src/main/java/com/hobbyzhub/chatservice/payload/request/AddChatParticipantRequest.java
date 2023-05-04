package com.hobbyzhub.chatservice.payload.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class AddChatParticipantRequest {
    private String chatId;
    private String userId;
    private String userName;
    private String userProfilePicLink;
}
