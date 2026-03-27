package com.serdyuchenko.socials.dto;

import java.util.UUID;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

// DTO для создания поста.
public record PostCreateRequestDto(
	@NotNull(message = "userId не может быть null")
	UUID userId,
	@NotBlank(message = "title не может быть пустым")
	String title,
	@NotBlank(message = "text не может быть пустым")
	String text
) {
}
