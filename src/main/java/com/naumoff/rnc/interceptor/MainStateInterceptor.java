package com.naumoff.rnc.interceptor;

import com.naumoff.rnc.services.menu.MainMenuService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.jetbrains.annotations.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

@Component
public class MainStateInterceptor implements HandlerInterceptor {

    final private MainMenuService mainMenuService;

    public MainStateInterceptor(
            MainMenuService mainMenuService
    ) {
        this.mainMenuService = mainMenuService;
    }

    @Override
    public boolean preHandle(
            HttpServletRequest request,
            HttpServletResponse response,
            Object handler
    ) {
        return true;
    }

    @Override
    public void postHandle(
            HttpServletRequest request,
            HttpServletResponse response,
            Object handler,
            @Nullable ModelAndView modelAndView
    ) {
        if (modelAndView == null) {
            return;
        }

        // @todo realize generated main menu
    }
}
