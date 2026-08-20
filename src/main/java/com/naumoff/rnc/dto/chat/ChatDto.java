package com.naumoff.rnc.dto.chat;

import lombok.Data;

@Data
public class ChatDto {

    private Boolean isActive;
    private String recipientName;
    private String lastMessage;
    private Long conversationId;
    private Long recipientId;

    public ChatDto(
            String recipientName,
            String lastMessage,
            Long conversationId,
            Long recipientId
    ) {
        this.recipientName = recipientName;
        this.lastMessage = lastMessage;
        this.conversationId = conversationId;
        isActive = false;
    }

    public Boolean getActive() {
        return isActive;
    }

    public void setActive(Boolean active) {
        isActive = active;
    }
}
