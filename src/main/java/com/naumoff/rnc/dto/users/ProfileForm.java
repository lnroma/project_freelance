package com.naumoff.rnc.dto.users;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProfileForm {

    // --- Личные данные ---
    @NotBlank(message = "Никнейм обязателен")
    private String nickname;

    @NotBlank(message = "Имя обязательное поле")
    private String firstName;

    @NotBlank(message = "Фамилия обязательное поле")
    private String lastName;

    @NotNull(message = "Пожалуйста, укажите пол")
    private String gender; // MALE / FEMALE / OTHER

    @NotNull(message = "Дата рождения обязательна")
    private LocalDate birthDate;

    @NotBlank(message = "Укажите, что ищете")
    private String relationshipType; // SERIOUS / FRIENDSHIP / DATING / OPEN

    // --- Локация и роль ---
    @NotNull(message = "Выберите город")
    private Long cityId;

    // Поля, которые видны только при наличии прав hasRoleEditor=true
    private String role; // USER / EXECUTOR / ADMIN
    @Min(value = 0, message = "Стоимость не может быть отрицательной")
    private Double costPerMonth;

    // --- Контакты ---
    @Email(message = "Некорректный формат email")
    private String email;

    private String phoneNumber;

    // --- Фото профиля ---
    // В форме это поле name="photo", тип MultipartFile
    private MultipartFile photo;

    // Опционально: если нужно хранить ID пользователя в форме (например, для скрытой проверки)
    private Long userId;
}