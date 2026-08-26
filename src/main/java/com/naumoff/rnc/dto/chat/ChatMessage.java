package com.naumoff.rnc.dto.chat;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ChatMessage {
    private Long conversationId;
    private Long senderId;
    private Long recipientId;
    private String text;
}