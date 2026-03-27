package com.serdyuchenko.socials.dto;

import java.time.OffsetDateTime;
import java.util.UUID;

import io.swagger.v3.oas.annotations.media.Schema;

// DTO ответа поста.
@Schema(description = "Ответ с данными поста")
public record PostResponseDto(
	@Schema(description = "Идентификатор поста")
	UUID id,
	@Schema(description = "Идентификатор автора")
	UUID userId,
	@Schema(description = "Заголовок поста")
	String title,
	@Schema(description = "Текст поста")
	String text,
	@Schema(description = "Дата и время создания поста")
	OffsetDateTime created
) {
}
