package com.hobbyzhub.chatservice.payload.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class NewPrivateChatModel {
    private String fromUserId;
    private String fromUserName;
    private String fromUserProfilePicLink;
    private String toUserId;
    private String toUserName;
    private String toUserProfilePicLink;
}
