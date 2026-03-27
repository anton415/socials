package com.serdyuchenko.socials.dto;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

import jakarta.validation.constraints.NotBlank;

import org.hibernate.validator.constraints.Length;

import io.swagger.v3.oas.annotations.media.Schema;

// DTO пользователя с его постами.
@Schema(description = "Пользователь с его постами")
public record UserPostsDto(
	@Schema(description = "Идентификатор пользователя")
	UUID userId,
	@Schema(description = "Имя пользователя")
	@NotBlank(message = "username не может быть пустым")
	@Length(min = 6,
			max = 10,
			message = "username должно быть не менее 6 и не более 10 символов")
	String username,
	@Schema(description = "Список постов пользователя")
	List<PostDto> posts
) {
	// DTO  поста в ответе пользователя.
	@Schema(description = "Пост пользователя")
	public record PostDto(
		@Schema(description = "Идентификатор поста")
		UUID id,
		@Schema(description = "Заголовок поста")
		String title,
		@Schema(description = "Текст поста")
		String text,
		@Schema(description = "Дата и время создания")
		OffsetDateTime created
	) {
	}
}
