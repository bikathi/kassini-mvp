package com.hobbyzhub.chatservice.payload.request;

import lombok.*;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class NewGroupChatModelRequest {
    private String chatName;

    // create will be the first to be added to the list of participants
    private String createdByUserId;
    private String createByUserName;
    private String userProfilePicLink;
}
