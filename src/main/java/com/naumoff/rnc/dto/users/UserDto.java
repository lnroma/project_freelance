package com.naumoff.rnc.dto.users;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class UserDto {
    @NotBlank(message = "Email обязательное поле")
    @Email(message = "Введенные данные не являются email'ом")
    private String email;

    @NotBlank(message = "Номер телефона обязательное поле")
    private String phoneNumber;

    @NotBlank(message = "Пароль обязательное поле")
    private String password;

    @NotBlank
    private String role;

    @DecimalMin(value = "0.0", message = "Ставка часа должна быть больше 0")
    private BigDecimal costPerMonth;

    private Integer cityId;
}
