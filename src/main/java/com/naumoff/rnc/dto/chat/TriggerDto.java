package com.naumoff.rnc.dto.chat;

import lombok.Data;

@Data
public class TriggerDto {
    private Long conversationId;

    public Long getConversationId() {
        return conversationId;
    }

    public void setConversationId(Long conversationId) {
        this.conversationId = conversationId;
    }
}
