package com.naumoff.rnc.interceptor;

import com.naumoff.rnc.database.entities.users.UserEntity;
import com.naumoff.rnc.dto.counters.CounterDto;
import com.naumoff.rnc.model.AuthenticatedUser;
import com.naumoff.rnc.services.chat.MessageService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

@Component
public class CountersInterceptor implements HandlerInterceptor {

    private final MessageService messageService;

    public CountersInterceptor(
            MessageService messageService
    ) {
        this.messageService = messageService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        return true; // продолжаем обработку
    }

    @Override
    public void postHandle(HttpServletRequest request,
                           HttpServletResponse response,
                           Object handler,
                           ModelAndView modelAndView) {
        if (modelAndView == null) {
            return;
        }

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        boolean isAuthenticated = auth != null
                && !(auth instanceof AnonymousAuthenticationToken)
                && auth.isAuthenticated();

        CounterDto counterDto = CounterDto
                .builder()
                .notReadMessages(0L)
                .build();

        if (isAuthenticated) {
            AuthenticatedUser authUser = (AuthenticatedUser) auth.getPrincipal();
            UserEntity currentUser = authUser.getEntity();

            counterDto.setNotReadMessages(messageService.getCountUnreadMessages(currentUser));
        }

        modelAndView.addObject("counterDto", counterDto);
    }
}
