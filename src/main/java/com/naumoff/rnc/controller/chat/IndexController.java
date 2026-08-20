package com.naumoff.rnc.controller.chat;

import com.naumoff.rnc.database.entities.users.UserEntity;
import com.naumoff.rnc.database.repository.users.UserRepository;
import com.naumoff.rnc.model.AuthenticatedUser;
import com.naumoff.rnc.services.chat.ChatService;
import com.naumoff.rnc.services.chat.ConversationsService;
import com.naumoff.rnc.services.chat.MessageService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class IndexController {

    private final ConversationsService conversationsService;
    private final MessageService messageService;
    private final ChatService chatService;
    private final UserRepository userRepository;

    public IndexController(
            ConversationsService conversationsService,
            MessageService messageService,
            ChatService chatService,
            UserRepository userRepository
    ) {
        this.conversationsService = conversationsService;
        this.messageService = messageService;
        this.chatService = chatService;
        this.userRepository = userRepository;
    }

    @GetMapping("/user/chat")
    public String getIndexChat(
//            @RequestParam(required = false) Long recipientId,
            Model model,
            @AuthenticationPrincipal
            AuthenticatedUser authUser
    ) {
        try {
            UserEntity currentUser = authUser.getEntity();
            UserEntity recipientUser = currentUser;
//        System.out.println("Recipient id " + recipientId);
//        if (recipientId != null) {
//            recipientUser = userRepository.findById(recipientId).get();
//        }
            if (this.chatService.getLastChats(currentUser) == null) {
                model.addAttribute("is_present_chats", false);
            }

            try {
                model.addAttribute("is_present_chats", true);
                model.addAttribute("conversations", this.chatService.getLastChats(currentUser));
                model.addAttribute("currentConversation", this.chatService.getActiveChat(
                        this.chatService.getLastChats(currentUser)
                ));
                model.addAttribute("messages", this.chatService.getLastMessageFromConversation(
                        this.chatService.getLastChats(currentUser)
                ));
                model.addAttribute("currentUser", currentUser);
                model.addAttribute("userTo", recipientUser);
            } catch (Exception e) {
                System.out.println(e.getMessage() + " " + e.getClass() + " " + e.getCause());
//            return "user/chat/index";
            }

            return "user/chat/index";
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "user/chat/index";
    }
}
