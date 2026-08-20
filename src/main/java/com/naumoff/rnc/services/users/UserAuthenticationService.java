package com.naumoff.rnc.services.users;

import com.naumoff.rnc.database.entities.users.UserEntity;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class UserAuthenticationService {

    final private AuthenticationManager authenticationManager;

    public UserAuthenticationService(
            AuthenticationManager authenticationManager
    ) {
        this.authenticationManager = authenticationManager;
    }

    public void authenticate(
            UserEntity user,
            HttpServletRequest request
    ) {

        // 2. Создаём аутентификацию
        UsernamePasswordAuthenticationToken authRequest =
                new UsernamePasswordAuthenticationToken(
                        user.getEmail(),
                        user.getPassword(),
                        Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"))
                );

        Authentication authResult;
        try {
            authResult = authenticationManager.authenticate(authRequest);
        } catch (AuthenticationException e) {
            // Если аутентификация не прошла — откатываем/удаляем пользователя или показываем ошибку
//            redirectAttributes.addFlashAttribute("error", "Не удалось авторизоваться после регистрации");
//            return "redirect:/registration";
            return;
        }

        // 3. Заполняем SecurityContext
        SecurityContextHolder.getContext().setAuthentication(authResult);

        // 4. (Опционально) явно сохраняем в HttpSession, если нужно
        HttpSession session = request.getSession(true);
        session.setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY,
                SecurityContextHolder.getContext());
    }
}
