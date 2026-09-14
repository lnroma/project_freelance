package com.naumoff.rnc.unit.users;

import com.naumoff.rnc.database.entities.users.UserEntity;
import com.naumoff.rnc.services.users.UserAuthenticationService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;

import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserAuthenticationServiceTest {

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpSession session;

    @InjectMocks
    private UserAuthenticationService userAuthenticationService;

    private UserEntity user;

    @BeforeEach
    void setUp() {
        // Сбрасываем SecurityContext перед каждым тестом
        SecurityContextHolder.clearContext();

        user = new UserEntity();
        user.setEmail("test@example.com");
        user.setPassword("secret-password");
    }

    @Test
    void authenticate_shouldAuthenticateAndSetSecurityContextAndSession() {
        Authentication mockAuth = mock(Authentication.class);
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(mockAuth);

        when(request.getSession(true)).thenReturn(session);

        userAuthenticationService.authenticate(user, request);

        // Проверяем, что был создан токен с email, паролем и ROLE_USER
        verify(authenticationManager).authenticate(argThat(token ->
                token.getPrincipal().equals(user.getEmail()) &&
                token.getCredentials().equals(user.getPassword()) &&
                token.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_USER"))
        ));

        // Проверяем, что SecurityContext заполнен
        Authentication currentAuth = SecurityContextHolder.getContext().getAuthentication();
        assertThat(currentAuth).isEqualTo(mockAuth);

        // Проверяем сохранение в сессии
        verify(session).setAttribute(
                eq(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY),
                any()
        );
    }

    @Test
    void authenticate_shouldNotSetSecurityContextOnAuthenticationFailure() {
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new org.springframework.security.authentication.BadCredentialsException("Bad credentials"));

        userAuthenticationService.authenticate(user, request);

        // Аутентификация не должна быть установлена
        Authentication currentAuth = SecurityContextHolder.getContext().getAuthentication();
        assertThat(currentAuth).isNull();

        // Сессия не должна быть изменена (или можно проверить, что setAttribute не вызван)
        verify(session, never()).setAttribute(anyString(), any());
    }

    @Test
    void authenticate_shouldCreateTokenWithCorrectAuthorities() {
        Authentication mockAuth = mock(Authentication.class);
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(mockAuth);
        when(request.getSession(true)).thenReturn(session);

        userAuthenticationService.authenticate(user, request);

        // Убеждаемся, что в токене ровно одна роль ROLE_USER
        verify(authenticationManager).authenticate(argThat(token -> {
            var authorities = token.getAuthorities();
            return authorities.size() == 1 &&
                    authorities.iterator().next().getAuthority().equals("ROLE_USER");
        }));
    }
}
