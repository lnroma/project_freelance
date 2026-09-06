package com.naumoff.rnc.controller.chat;

import com.naumoff.rnc.database.entities.chat.Conversation;
import com.naumoff.rnc.database.entities.chat.ConversationMessage;
import com.naumoff.rnc.dto.menu.MenuCollectionDto;
import com.naumoff.rnc.model.AuthenticatedUser;
import com.naumoff.rnc.services.chat.ChatService;
import com.naumoff.rnc.services.chat.ConversationsService;
import com.naumoff.rnc.services.chat.MessageService;
import com.naumoff.rnc.services.menu.MainMenuService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller
public class FavoriteController {

    private final ChatService chatService;
    private final MessageService messageService;
    private final ConversationsService conversationsService;
    private final MainMenuService mainMenuService;

    public FavoriteController(
            MessageService messageService,
            ConversationsService conversationsService,
            ChatService chatService,
            MainMenuService mainMenuService
    ) {
        this.messageService = messageService;
        this.conversationsService = conversationsService;
        this.chatService = chatService;
        this.mainMenuService = mainMenuService;
    }

    @GetMapping("/user/chat/{conversationId}/favorites")
    public String getAllFavoriteMessageForConversation(
            @PathVariable(name = "conversationId") Long conversationId,
            @AuthenticationPrincipal
            AuthenticatedUser authenticatedUser,
            Model model
    ) {
        Conversation currentConversation = conversationsService.getConversationById(conversationId);
        List<ConversationMessage> messages = messageService.getConversationMessagesIsFavorite(currentConversation);
        model.addAttribute("messages", messages);
        model.addAttribute("currentConversation", currentConversation);
        model.addAttribute("conversations", chatService.getLastChats(authenticatedUser.getEntity()));
        model.addAttribute("isFavorite", true);
        model.addAttribute("currentUser", authenticatedUser.getEntity());

        MenuCollectionDto menuCollectionDto = mainMenuService.getMenuCollectionDto();
        mainMenuService.assignMenuToTemplate(model, menuCollectionDto);

        return "/user/chat/favorites";
    }

    @GetMapping("/user/message/{messageId}/add/to/favorite")
    public String addMessageToFavorite(
            @PathVariable(name = "messageId") Long messageId,
            @AuthenticationPrincipal
            AuthenticatedUser authenticatedUser
    ) {
        ConversationMessage message = messageService.getMessageById(messageId);

        Conversation currentConversation = message.getConversation();

        if (!conversationsService.checkConversationIsUser(authenticatedUser.getEntity(), currentConversation)) {
            // @todo return to message box with error
            return "redirect:/";
        }

        messageService.markMessageIsFavorite(message);

        return "redirect:/user/chat/" + currentConversation.getId();
    }
}
