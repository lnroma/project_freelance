package com.naumoff.rnc.interceptor;

import com.naumoff.rnc.model.AuthenticatedUser;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

@Component
public class AuthInterceptor implements HandlerInterceptor {

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


        modelAndView.addObject("isAuthenticated", isAuthenticated);
        modelAndView.addObject("authentication", auth);

        if (isAuthenticated) {
            AuthenticatedUser authUser = (AuthenticatedUser) auth.getPrincipal();
            modelAndView.addObject("user", authUser.getEntity());
        }
        // можно положить userDetails, username и т.д.
    }
}