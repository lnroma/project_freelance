package com.naumoff.rnc.controller.chat;

import com.naumoff.rnc.services.chat.ConversationsService;
import org.springframework.stereotype.Controller;

@Controller
public class StompConversationController {
    private final ConversationsService conversationsService;

    public StompConversationController(
            ConversationsService conversationsService
    ) {
        this.conversationsService = conversationsService;
    }


}
