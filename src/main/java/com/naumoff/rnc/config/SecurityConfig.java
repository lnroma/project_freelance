package com.naumoff.rnc.services;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    final private UserDetailsService userDetailsService;
    final private AuthenticationConfiguration authenticationConfiguration;

    public SecurityConfig(
            UserDetailsService userDetailsService,
            AuthenticationConfiguration authenticationConfiguration
    ) {
        this.userDetailsService = userDetailsService;
        this.authenticationConfiguration = authenticationConfiguration;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager() throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                                // Все URL доступны всем, но с проверкой ролей
                                .requestMatchers("/**").permitAll()
                                .requestMatchers("ws/**").permitAll()
                                // Конкретные ограничения по ролям
                                .requestMatchers("/admin/**").hasRole("ADMIN")
                                .requestMatchers("/moderator/**").hasAnyRole("MODERATOR", "ADMIN")
                                .requestMatchers("/premium/**").hasRole("PREMIUM")
                                .requestMatchers("/dashboard/**").authenticated()
                        // Можно добавить больше правил
                )
                .formLogin(form -> form
                        .loginPage("/login")
                        .usernameParameter("username")
                        .passwordParameter("password")
                        .defaultSuccessUrl("/dashboard", true)
                        .failureUrl("/login?error=true")
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutSuccessUrl("/login")
                        .permitAll()
                )
//                .csrf(csrf -> csrf.disable())
        ;
        return http.build();
    }
}
