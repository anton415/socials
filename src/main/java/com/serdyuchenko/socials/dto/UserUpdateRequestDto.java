package com.serdyuchenko.socials.dto;

import java.util.UUID;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

// DTO для обновления пользователя.
public record UserUpdateRequestDto(
	@NotNull(message = "id не может быть null")
	UUID id,
	@NotBlank(message = "username не может быть пустым")
	String username,
	@NotBlank(message = "email не может быть пустым")
	@Email(message = "email имеет некорректный формат")
	String email,
	@NotBlank(message = "password не может быть пустым")
	String password
) {
}
