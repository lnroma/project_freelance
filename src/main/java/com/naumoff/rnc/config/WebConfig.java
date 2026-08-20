package com.naumoff.rnc.config;

import com.naumoff.rnc.interceptor.AuthInterceptor;
import com.naumoff.rnc.interceptor.CsrfInterceptor;
import com.naumoff.rnc.interceptor.CurrentQueryInterceptor;
import com.naumoff.rnc.interceptor.SeoInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    final private AuthInterceptor authInterceptor;
    final private SeoInterceptor seoInterceptor;
    final private CsrfInterceptor csrfInterceptor;
    final private CurrentQueryInterceptor currentQueryInterceptor;

    public WebConfig(
            AuthInterceptor authInterceptor,
            SeoInterceptor seoInterceptor,
            CsrfInterceptor csrfInterceptor,
            CurrentQueryInterceptor currentQueryInterceptor
    ) {
        this.authInterceptor = authInterceptor;
        this.seoInterceptor = seoInterceptor;
        this.csrfInterceptor = csrfInterceptor;
        this.currentQueryInterceptor = currentQueryInterceptor;
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/registration")
                .allowedOriginPatterns("*")
                .allowedMethods("POST", "GET", "OPTIONS")
                .allowCredentials(false)
                .maxAge(300);
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(authInterceptor);
        registry.addInterceptor(seoInterceptor);
        registry.addInterceptor(csrfInterceptor);
        registry.addInterceptor(currentQueryInterceptor);
    }
}
