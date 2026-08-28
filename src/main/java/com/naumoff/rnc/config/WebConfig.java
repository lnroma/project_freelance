package com.naumoff.rnc.config;

import com.naumoff.rnc.interceptor.*;
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
    final private CountersInterceptor countersInterceptor;

    public WebConfig(
            AuthInterceptor authInterceptor,
            SeoInterceptor seoInterceptor,
            CsrfInterceptor csrfInterceptor,
            CurrentQueryInterceptor currentQueryInterceptor,
            CountersInterceptor countersInterceptor
    ) {
        this.authInterceptor = authInterceptor;
        this.seoInterceptor = seoInterceptor;
        this.csrfInterceptor = csrfInterceptor;
        this.currentQueryInterceptor = currentQueryInterceptor;
        this.countersInterceptor = countersInterceptor;
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
        registry.addInterceptor(countersInterceptor);
    }
}
