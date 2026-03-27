package com.serdyuchenko.socials.dto;

import java.util.UUID;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import io.swagger.v3.oas.annotations.media.Schema;

// DTO для обновления пользователя.
@Schema(description = "Запрос на обновление пользователя")
public record UserUpdateRequestDto(
	@Schema(description = "Идентификатор пользователя")
	@NotNull(message = "id не может быть null")
	UUID id,
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
