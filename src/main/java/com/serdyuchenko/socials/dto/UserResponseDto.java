package com.serdyuchenko.socials.dto;

import java.util.UUID;

import io.swagger.v3.oas.annotations.media.Schema;

// DTO ответа пользователя.
@Schema(description = "Ответ с данными пользователя")
public record UserResponseDto(
	@Schema(description = "Идентификатор пользователя")
	UUID id,
	@Schema(description = "Имя пользователя")
	String username,
	@Schema(description = "Email пользователя")
	String email
) {
}
