package com.serdyuchenko.socials.dto;

import java.util.UUID;

// DTO ответа пользователя.
public record UserResponseDto(
	UUID id,
	String username,
	String email
) {
}
