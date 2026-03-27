package com.serdyuchenko.socials.dto;

import java.util.UUID;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import io.swagger.v3.oas.annotations.media.Schema;

// DTO для создания поста.
@Schema(description = "Запрос на создание поста")
public record PostCreateRequestDto(
	@Schema(description = "Идентификатор автора поста")
	@NotNull(message = "userId не может быть null")
	UUID userId,
	@Schema(description = "Заголовок поста", example = "Мой первый пост")
	@NotBlank(message = "title не может быть пустым")
	String title,
	@Schema(description = "Текст поста", example = "Привет, мир!")
	@NotBlank(message = "text не может быть пустым")
	String text
) {
}
