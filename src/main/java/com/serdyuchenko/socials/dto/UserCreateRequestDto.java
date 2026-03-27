package com.serdyuchenko.socials.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

// DTO для создания пользователя.
public record UserCreateRequestDto(
	@NotBlank(message = "username не может быть пустым")
	String username,
	@NotBlank(message = "email не может быть пустым")
	@Email(message = "email имеет некорректный формат")
	String email,
	@NotBlank(message = "password не может быть пустым")
	String password
) {
}
