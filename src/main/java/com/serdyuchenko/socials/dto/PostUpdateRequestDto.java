package com.serdyuchenko.socials.dto;

import java.util.UUID;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import io.swagger.v3.oas.annotations.media.Schema;

// DTO для обновления поста.
@Schema(description = "Запрос на обновление поста")
public record PostUpdateRequestDto(
	@Schema(description = "Идентификатор поста")
	@NotNull(message = "id не может быть null")
	UUID id,
	@Schema(description = "Заголовок поста", example = "Обновленный заголовок")
	@NotBlank(message = "title не может быть пустым")
	String title,
	@Schema(description = "Текст поста", example = "Обновленный текст")
	@NotBlank(message = "text не может быть пустым")
	String text
) {
}
