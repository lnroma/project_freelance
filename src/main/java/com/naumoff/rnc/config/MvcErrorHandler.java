package com.naumoff.rnc.config;

import com.naumoff.rnc.controller.exceptions.NotFoundException;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.server.ResponseStatusException;

@ControllerAdvice
public class MvcErrorHandler {

    /**
     * Обрабатывает конкретно 404 (Not Found)
     * Сюда можно кидать NotFoundException в контроллерах чата/профилей
     */
    @ExceptionHandler(NotFoundException.class)
    public String handleNotFound(NotFoundException ex, Model model) {
        System.out.println("Not found exception handle");

        model.addAttribute("errorCode", 404);
        model.addAttribute("errorMessage", ex.getMessage());
        // Возвращаем имя шаблона без расширения: templates/404.ftl
        return "system/404";
    }

    /**
     * Обрабатывает любые другие HTTP-ошибки (например, 500)
     * Ловит ResponseStatusException, который Spring кидает сам, 
     * если ты делаешь throw new ResponseStatusException(HttpStatus.NOT_FOUND)
     */
    @ExceptionHandler(ResponseStatusException.class)
    public String handleHttpError(ResponseStatusException ex, Model model) {
        System.out.println("handle http error ");
        int status = ex.getStatusCode().value();
        model.addAttribute("errorCode", status);
        model.addAttribute("errorMessage", ex.getReason());
        
        if (status == 404) {
            return "system/404"; // Если вдруг 404 прилетит сюда
        }
        return "system/error"; // Общий шаблон для остальных ошибок
    }

    @ExceptionHandler(Exception.class) // ловим вообще всё, чтобы проверить, работает ли хендлер
    public String catchAll(Exception ex, Model model) {
        System.out.println("!!! GlobalErrorHandler поймал исключение: " + ex.getClass().getName());
        model.addAttribute("errorMessage", "Ошибка перехвачена глобально: " + ex.getMessage());
        ex.printStackTrace();
        return "system/error";
    }
}
