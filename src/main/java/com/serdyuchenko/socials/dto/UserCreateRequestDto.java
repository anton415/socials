package com.serdyuchenko.socials.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

import io.swagger.v3.oas.annotations.media.Schema;

// DTO для создания пользователя.
@Schema(description = "Запрос на создание пользователя")
public record UserCreateRequestDto(
	@Schema(description = "Имя пользователя", example = "john_doe")
	@NotBlank(message = "username не может быть пустым")
	String username,
	@Schema(description = "Email пользователя", example = "john@example.com")
	@NotBlank(message = "email не может быть пустым")
	@Email(message = "email имеет некорректный формат")
	String email,
	@Schema(description = "Пароль пользователя", example = "secret123")
	@NotBlank(message = "password не может быть пустым")
	String password
) {
}
