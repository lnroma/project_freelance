package com.naumoff.rnc.controller.chat;

import com.naumoff.rnc.database.entities.chat.Conversation;
import com.naumoff.rnc.database.entities.chat.ConversationMessage;
import com.naumoff.rnc.database.entities.users.UserEntity;
import com.naumoff.rnc.database.repository.users.UserRepository;
import com.naumoff.rnc.dto.chat.ChatMessage;
import com.naumoff.rnc.dto.menu.MenuCollectionDto;
import com.naumoff.rnc.model.AuthenticatedUser;
import com.naumoff.rnc.services.chat.ChatService;
import com.naumoff.rnc.services.chat.ConversationsService;
import com.naumoff.rnc.services.chat.MessageService;
import com.naumoff.rnc.services.chat.SaveMessageService;
import com.naumoff.rnc.services.menu.MainMenuService;
import org.springframework.security.core.AuthenticatedPrincipal;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Controller
public class IndexController {

    private final ConversationsService conversationsService;
    private final MessageService messageService;
    private final ChatService chatService;
    private final UserRepository userRepository;
    private final MainMenuService mainMenuService;

    public IndexController(
            ConversationsService conversationsService,
            MessageService messageService,
            ChatService chatService,
            UserRepository userRepository,
            MainMenuService mainMenuService
    ) {
        this.conversationsService = conversationsService;
        this.messageService = messageService;
        this.chatService = chatService;
        this.userRepository = userRepository;
        this.mainMenuService = mainMenuService;
    }

    @GetMapping("/user/chat/{id}")
    public String getChatRoom(
            Model model,
            @AuthenticationPrincipal
            AuthenticatedUser authUser,
            @PathVariable
            Long id
    ) {
        UserEntity currentUser = authUser.getEntity();
        Conversation conversation = conversationsService.createConversationForSelfMessages(currentUser);

        Conversation currentConversation = conversationsService.getConversationById(id);

        UserEntity recipientUser;
        if (currentConversation.getUserFromEntity().equals(currentUser)) {
            recipientUser = currentConversation.getUserToEntity();
        } else {
            recipientUser = currentConversation.getUserFromEntity();
        }

        model.addAttribute("userTo", recipientUser);
        model.addAttribute("currentUser", currentUser);

        model.addAttribute("currentConversation", currentConversation);

        List<ConversationMessage> messages = messageService.getConversationMessages(currentConversation);
        model.addAttribute("messages", messages);
        model.addAttribute("conversations", chatService.getLastChats(currentUser));

        model.addAttribute("isFavorite", false);

        conversationsService.setIsReadAllMessagesInConversation(currentConversation, authUser.getEntity());

        MenuCollectionDto menuCollectionDto = mainMenuService.getMenuCollectionDto();
        mainMenuService.assignMenuToTemplate(model, menuCollectionDto);

        return "user/chat/index";
    }

    @GetMapping("/user/chat/")
    public String getIndexChat(
//            @RequestParam(required = false) Long recipientId,
            Model model,
            @AuthenticationPrincipal
            AuthenticatedUser authUser
    ) {

        UserEntity currentUser = authUser.getEntity();
        UserEntity recipientUser = currentUser;

        Conversation conversation = conversationsService.createConversationForSelfMessages(currentUser);

        return "redirect:/user/chat/" + conversation.getId();
    }

    @GetMapping(value = "/send/message/to/{id}")
    public String sendToMessage(
            @PathVariable
            Long id,
            @AuthenticationPrincipal
            AuthenticatedUser authUser
    ) {
        UserEntity userFrom = authUser.getEntity();
        UserEntity userTo = userRepository.findById(id).get();

        Conversation conversation = conversationsService.createConversation(userFrom, userTo);

        return "redirect:/user/chat/" + conversation.getId();
    }
}
