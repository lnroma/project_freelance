package com.naumoff.rnc.interceptor;

import com.naumoff.rnc.database.entities.pages.SeoPage;
import com.naumoff.rnc.services.pages.SeoPagesService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

@Component
public class SeoInterceptor implements HandlerInterceptor {

    private final SeoPagesService seoPagesService;

    public SeoInterceptor(
            SeoPagesService seoPagesService
    ) {
        this.seoPagesService = seoPagesService;
    }

    @Override
    public boolean preHandle(
            HttpServletRequest request,
            HttpServletResponse response,
            Object handler
    ) {
        return true; // продолжаем обработку
    }

    @Override
    public void postHandle(
            HttpServletRequest request,
            HttpServletResponse response,
            Object handler,
            ModelAndView modelAndView
    ) {
        if (modelAndView == null) {
            return;
        }

        String slug = request.getRequestURI();

        SeoPage seoPage = seoPagesService.getSeoMetainformation(slug);
        System.out.println("slug for this page: " + slug);

        modelAndView.addObject("seo", seoPage);
    }
}
