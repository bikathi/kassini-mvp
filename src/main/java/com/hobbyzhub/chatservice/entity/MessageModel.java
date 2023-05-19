package com.hobbyzhub.chatservice.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;

@Document(collection = "message-model-collection")
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MessageModel implements Serializable {
    /**
     * The id of the chat that the message belongs to
     */
    @Id
    private String chatModelId;

    /**
     * The id of the message
     */
    private String messageModelId;
    private String messageString;
    private String fromUserId;

    // the destination ID can be a queue or a topic
    private String toDestinationId;

    // DD-MM-YY, HH:MM
    private String dateTimeSent;
}
