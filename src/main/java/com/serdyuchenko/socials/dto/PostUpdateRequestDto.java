package com.serdyuchenko.socials.dto;

import java.util.UUID;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

// DTO для обновления поста.
public record PostUpdateRequestDto(
	@NotNull(message = "id не может быть null")
	UUID id,
	@NotBlank(message = "title не может быть пустым")
	String title,
	@NotBlank(message = "text не может быть пустым")
	String text
) {
}
